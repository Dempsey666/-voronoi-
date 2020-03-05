# -voronoi-
龙龙粉丝后援团专属

#2.29重新阅读论文，找到https://blog.csdn.net/weixin_42943114/article/details/82262122?depth_1-utm_source=distribute.pc_relevant.none-task&utm_source=distribute.pc_relevant.none-task
作为索引了解代码思路

#3.1 
配置idea环境，安装了maven、mybatis和mysql8.0.1

由于maven不能编译maven项目下src路径中的xml文件，以及mysql8.0.1换了字符编码，配置了近一天环境 
 
进展：  
1、用sql语言内连接清洗后的原始数据和基站静态数据，制作含有（time、imsi、longitude、latitude）四个字段的一张表用于时间切片  
2、完成初始的pojo创建，准备好数据库所需的所有表-类映射以及基础数据类的创建  
3、完成mybatis与mysql的连接，完成读取数据指令
（注：原有的cell_id过大，被舍弃，在数据库读取的过程中所有基站被定义新id，方便程序编写）
      
#3.2  
进展：  
1、重写Point的compare方法，完成所有点，经度为主序，纬度为从序的排列，为三角剖分做准备  
2、完成超级三角形的构建  

#3.3  
进展：  
1、完成基础的三角剖分  
2、简单跑了一下算法，改了两个bug  
3、发现算法复杂度为O(n^2)，但是复杂度改不了，只能在计算中及时释放不需要的数据  
4、对最复杂的部分，每次都要开辟大量空间，执行重复的计算，所以写了多线程

#3.4
写了一早上，发现还是不行，算的太慢了，决定换一种算法  

对昨天进行纠正，更新算法最复杂的函数为buffer去重.  
调整算法后，十几秒就算完了2.8K个点，但是还不能验证正确性  

在c#文件中添加了一个显示三角形总数的代码，发现对n个点，最终构成2n+1个三角形，而我的程序对2793个点构成72000+个三角形  

发现问题根源：基站点集中有重复的点，这些点在算法中无法判断，导致缓存数组中数据量特别多，还不能通过条件remove掉  
解决方案：在读入后对sites进行去重，再写入points，发现点集个数降为2696，所以有97个重复点  
神奇的事情出现了，三角形数据量不减反增到了78389个？？？

发现问题：有些点的buffer为0  
很清楚地可以看出，这些必定是longitude相同，latitude不同的点  
思考后发现，这并没有什么关系，只是在圆外而已，不影响程序 

发现问题：有些时候，buffer去重后，减去了奇数个。而两圆相交，必定只有一条公共弦，buffer中一去去两个，不可能有奇数条  
难道会有三圆相交，出现一条公共弦的情况？  
在进一步测试中发现问题，在有重复的list中有n条，出现了去重后删除数量小于2n的情况，且非常普遍  
发现问题所在：边的equals条件重写后，当两点相同时返回true，但是，删除时，没有顺序  
解决方案：检查重复时，遇到相同的edge，同时将两个edge加入重复list

最终解决：发现基站数据中重复的点并没有根本去重完全，因为数据库中的数据是乱序的，我是用倒序遍历删除重复点的  
修改bug后，基站数量确定为1519，三角形数为3022，估计还有17个三角形还没有进行凸边补全  
晚上再做（15:37），去吃中饭了

#3.5
第一次完成凸边修饰后，三角形数量增加了42个至3064，远远超过了预期，需要检查一下  
DEBUG发现大量重复  
发现去除函数有问题  
修复去重函数，现在已经可以正常去重，三角形数量降至3050，仍有11个超过预期  

手动数，发现21个需要去除的三角形

发现少写了一个break，现数量降至3029，比预期少了10个，考虑到算法的不一致性，我认为暂时没有问题了