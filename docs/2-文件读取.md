# Java第二轮考核

> 该考核内容主要参考自[2024年福州大学软件工程实践第二次作业]([软件工程第二次作业--文件读取-CSDN社区](https://bbs.csdn.net/topics/618087255))



## 任务

完成对**世界游泳锦标赛跳水项目**相关数据的收集，并实现一个能够对赛事数据进行统计的控制台程序，项目包括基本功能的实现、单元测试、性能分析与改进，以及README.md撰写。

### 需求

实现一个命令行程序，不妨称之为**DWASearch**。
本次作业所需数据均爬取自**世界游泳锦标赛官网的跳水项目**[比赛结果](https://www.worldaquatics.com/competitions/3337/66th-international-divers-day-rostock/results?event=108c795d-5e4f-4dc6-acea-0bc70bfd1928)和[参赛选手信息](https://www.worldaquatics.com/competitions/3337/66th-international-divers-day-rostock/athletes?gender=&countryId=)【该爬取行为仅用于课程教学】 

数据收集部分可以参考往届优秀作业[[参考链接\]](https://blog.csdn.net/CowBoyHS/article/details/129327279?csdn_share_tail={"type"%3A"blog"%2C"rType"%3A"article"%2C"rId"%3A"129327279"%2C"source"%3A"CowBoyHS"})

> 如需http请求工具或者Json解析工具，可以使用**Maven**方式导入



### 1. 基本功能

假设有一个软件可以输出2024世界游泳锦标赛跳水项目的选手信息和比赛结果。
输入指令和输出文件以命令行参数传入。例如我们在命令行窗口(cmd)中输入：

```bash
Java -jar DWASearch.jar input.txt output.txt
```



#### 1.1 输出所有选手信息

当input.txt的内容为

```javascript
players
```

则会输出**参与世界游泳锦标赛跳水项目的所有选手信息**到**output.txt**，输出格式如下：

1. 其中`Full Name`对应选手全名，`Gender`为选手性别, `Country`为国籍。
2. 换行使用'\n'，编码统一使用UTF-8。
3. 顺序以国籍为首要关键字升序、选手的名(Last Name)为次要关键字升序排序(若爬取的数据已经排序完成则仅需要依次提取需要的信息，不必编写排序过程的代码)。
4. 每输出一个选手的相关信息后，以5个“-”单独成行作为分割线，最后一个选手信息输出后仍要输出一行分割线。
5. 输出的内容中除选手名称和选手国籍可能存在空格，其他信息不得增加多于的空格或者其他符号。

输出格式：

```javascript
Full Name:string
Gender:string
Country:string 
-----
...
-----
Full Name:string
Gender:string
Country:string 
-----
```

输出样例：

```javascript
Full Name:HART Alexander
Gender:Male
Country:Austria
-----
Full Name:LOTFI Dariush
Gender:Male
Country:Austria
-----
...
-----
Full Name:DICK Elaena
Gender:Female
Country:Canada
-----
```

---



#### 1.2 输出决赛每个运动项目结果

当input.txt的内容为

```javascript
result women 1m springboard
```

则会输出**女子1m跳板**的决赛结果到**output.txt**，输出格式如下:

1. `Full Name`对应选手姓名。
2. `Rank`为排名。格式如`'1'`。
3. `Score`表示决赛中该选手的成绩。格式如`'score1 + score2 + score3 + ··· + score7 = totalPoint'`。

输出格式：

```javascript
Full Name:string
Rank:string
Score:string
-----
...
-----
Full Name:string
Rank:string
Score:string
-----
```

输出样例：

```javascript
Full Name:MULLER Jette
Rank:1
Score:51.60 + 52.00 + 51.75 + 46.80 + 46.80 = 248.95
-----
Full Name:ROLLINSON Amy
Rank:2
Score:46.00 + 42.90 + 50.70 + 54.00 + 46.80 = 240.40
-----
...
-----
Full Name:SANTIAGO Dominique
Rank:12
Score:42.00 + 18.20 + 35.70 + 34.50 + 32.55 = 162.95
-----
```

---



#### 1.3 输出详细结果

当input.txt的内容为

```
result women 10m platform detail
```

则会输出世界游泳锦标赛跳水项目**女子10m跳台**的所有比赛结果到**output.txt**，输出格式如下:

1. `Full Name`对应选手姓名。
2. `Rank`为排名。格式如`'1 | 2 | 3'`表示初赛排名为1，半决赛排名为2，决赛排名为3。部分比赛若无半决赛或初赛又或者选手未参加半决赛或决赛的则用`'*'`占位表示，如`'* | * | 1'`。
3. `Preliminary Score`表示初赛中该选手的成绩。格式如`'score1 + score2 + score3 + ··· + score7 = totalPoint'`。若无初赛则用`'*'`表示。
4. `Semifinal Score`表示半决赛中该选手的成绩。格式如`'score1 + score2 + score3 + ··· + score7 = totalPoint'`。若无半决赛或者选手未参加则用`'*'`表示。
5. `Final Score`表示决赛中该选手的成绩。格式如`'score1 + score2 + score3 + ··· + score7 = totalPoint'`。若选手未参加则用`'*'`表示。
6. 输出顺序按照选手**第一次比赛的排名顺序**输出。
7. 注意空格出现的位置，不能有多于的空格，否则会被判定为格式错误。
8. **[补充]** 如果遇到**双人项目**，选手姓名格式命名为`'A & B'`按照选手名（Last Name）从小到大排序，例如`'JACHIM Filip & LUKASZEWICZ Robert'`

输出格式：

```javascript
Full Name:string
Rank:string
Preliminary Score:string
Semifinal Score:string
Final Score:string
-----
...
-----
Full Name:string
Rank:string
Preliminary Score:string
Semifinal Score:string
Final Score:string
-----
```

输出样例：

```javascript
Full Name:FUNG Katelyn
Rank:1 | 4 | 2
Preliminary Score:60.20 + 56.00 + 57.60 + 54.00 + 70.40 = 298.20
Semifinal Score:46.20 + 30.80 + 68.80 + 61.50 + 67.20 = 274.50
Final Score:58.80 + 54.60 + 57.60 + 54.00 + 72.00 = 297.00
-----
...
-----
Full Name:SANTIAGO Dominique
Rank:14 | 11 | 11
Preliminary Score:39.20 + 21.00 + 41.85 + 31.20 + 13.05 = 146.30
Semifinal Score:42.00 + 22.50 + 17.55 + 42.90 + 39.15 = 164.10
Final Score:42.00 + 28.50 + 20.25 + 41.60 + 44.95 = 177.30
-----
```

**补充说明：**
对于**input.txt**，有可能会出现多行输入的样例，例如：

```javascript
players
result women 1m springboard
result women 1m springboard
players
```

此时的输出文件**output.txt**中的内容为：

```javascript
Full Name:HART Alexander
Gender:Male
Country:Austria 
-----
Full Name:LOTFI Dariush
Gender:Male
Country:Austria
-----
...
-----
Full Name:MULLER Jette
Rank:1
Score:51.60 + 52.00 + 51.75 + 46.80 + 46.80 = 248.95
-----
Full Name:ROLLINSON Amy
Rank:2
Score:46.00 + 42.90 + 50.70 + 54.00 + 46.80 = 240.40
-----
...
-----
Full Name:MULLER Jette
Rank:1
Score:51.60 + 52.00 + 51.75 + 46.80 + 46.80 = 248.95
-----
Full Name:ROLLINSON Amy
Rank:2
Score:46.00 + 42.90 + 50.70 + 54.00 + 46.80 = 240.40
-----
...
-----
Full Name:HART Alexander
Gender:Male
Country:Austria 
-----
Full Name:LOTFI Dariush
Gender:Male
Country:Austria
-----
...
-----
Full Name:MYALIN Igor
Gender:Male
Country:Uzbekistan
-----
```

其中，每个指令的输出紧贴上一个指令的输出，**无需空行**。

**指令区分大小写**，指令中**所有的英文字母都采用小写的形式**。
假如输入无法处理的指令，例如：

1. 无法识别的指令，则输出`Error`

2. result后的比赛项目名称应为如下这些名称之一，如果不正确，则输出`N/A`

   ```js
   women 1m springboard
   women 3m springboard
   women 10m platform
   women 3m synchronised
   women 10m synchronised
   men 1m springboard
   men 3m springboard
   men 10m platform
   men 3m synchronised
   men 10m synchronised
   ```

3. 在比赛项目名称后只能加上'detail'字符，如果字符不正确，则输出`N/A`。

input.txt样例：

```javascript
player
Players
resultwomen 1m springboard
result women 10m springboard
result sss
result detail
result women 1m springboard details
result men 10m     synchronised
players
```

output.txt输出：

```javascript
Error
-----
Error
-----
Error
-----
N/A
-----
N/A
-----
N/A
-----
N/A
-----
N/A
-----
Full Name:HART Alexander
Gender:Male
Country:Austria 
-----
Full Name:LOTFI Dariush
Gender:Male
Country:Austria
-----
...
-----
Full Name:MYALIN Igor
Gender:Male
Country:Uzbekistan
-----
```

---



### 2. 接口封装

你是否有发现上面的代码会有这样一个问题：代码散落在各个函数中，很难剥离出来作为一个独立的模块运行以满足不同的需求。

这些代码的种类不同，混杂在一起对于后期的维护扩展很不友好，所以它们的组织结构就需要精心的整理和优化。

为了提高代码的可维护性和可扩展性，需要将基本功能独立成一个模块，称之为"Core模块"。这个模块包括两个基本功能：

1. **输出所有选手信息**
2. **输出每个比赛项目的结果**

这样的设计使得命令行能够共享相同的代码，通过定义清晰的API，实现与其他模块的交流。

API设计应考虑以下几点：

- **清晰的函数命名：** 使函数名称表达其功能，方便其他开发者理解。
- **参数设计：** 确定需要传递的参数类型和数量，使接口简洁而有用。
- **错误处理：** 考虑可能的错误情况，设计良好的错误处理机制。

参考代码：

```java
public class CoreModule {
    // 输出所有选手信息
    public void displayAllPlayersInfo() {
        // 实现代码...
    }

    // 输出每个比赛项目的结果
    public void displayResultsForEachEvent() {
        // 实现代码...
    }
    
    .......
}

```

这个模块至少可以在几个地方使用：

- 命令行测试程序使用
- 在单元测试框架下使用



### 3. 单元测试和性能分析

要设计**至少10个测试用例**，确保你的程序能够正确处理各种情况。

> 推荐使用junit实现单元测试

请阅读邹欣老师的博客：[单元测试和回归测试](https://www.cnblogs.com/xinz/archive/2011/11/20/2255830.html)，编写程序的单元测试



### 4. 项目结构

1. 在src目录下必须有名为`DWASearch.java`文件，且DWASearch.java中包含 `public static void main(String[] args) `方法
2. 请将Java代码打包成jar

```
|- src
 |- DWASearch.java		主程序，可以从命令行接收参数；确保文件名一致、区分大小写
 |- Lib.java			包含其它自定义函数，可以有多个，对名字不做要求
 |- data 				文件夹，存放程序的数据
|- DWASearch.jar
|- README.md			描述你的项目，包括如何运行、功能简介、作业链接等
```

**注意：**

README.md 需要包括一下内容：

1. 记录**模块接口的设计与实现过程**。设计包括代码如何组织，比如会有几个类，几个函数，他们之间关系如何，关键函数是否需要画出流程图？说明你的算法的关键（不必列出源代码），以及独到之处。
2. 计算模块接口部分的**性能改进**。
3. 计算模块部分**单元测试展示**。
4. 计算模块部分**异常处理说明**。
