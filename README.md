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