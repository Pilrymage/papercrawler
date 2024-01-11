# Java 论文网站爬虫
## 1. 作业要求
爬取论文网站，批量采集论文信息。

## 2. 作业环境
- 操作系统：debian 12
- 数据库：sqlite3
- java版本：java 17
- 主要依赖：selenium, sqlite-jdbc, jsoup, javafx

## 3. 作业内容
### 3.1 爬取论文网站
主要使用 Selenium 爬取，
爬取的网站为论文网、百度学术、NSTL以及中国知网，爬取的信息包括论文标题、作者、邮箱、来源、时间等信息。
同时，基于javaFX实现了一个简单的GUI，可以通过GUI进行爬取论文信息的操作。
可以输入最大爬取条数，以及爬取的关键词，点击“开始爬取”，爬取的论文信息会存储在sqlite3数据库中。
下方的“刷新”按钮可以刷新数据库中的信息，并展现在表格中。

## 3.2 数据库增删改查，导入导出
当点击一行时，可以在下方的文本框中显示该行的信息，可以对该行进行修改，点击“修改”按钮即可修改该行的信息。
点击“删除”按钮即可删除该行的信息。
点击“添加”按钮即可添加一行信息，添加的信息会存储在数据库中。
上方的文本框可以输入关键词，点击“搜索”按钮即可查询数据库中的信息，查询的结果会展现在表格中。

点击目录树中的“导入”按钮，可以导入一个csv文件，导入的文件中的数据会添加在数据库后。
点击目录树中的“导出”按钮，可以导出一个csv文件。
以上的操作都可以通过GUI进行操作。

# 4.作业运行

## 4.1 sql create
CREATE TABLE "paper" (
	"id"	INTEGER,
	"title"	TEXT NOT NULL,
	"author"	TEXT NOT NULL,
	"email"	TEXT,
	"source"	TEXT,
	"time"	TEXT,
	PRIMARY KEY("id" AUTOINCREMENT)
)

# 5. GPL License
本项目遵循GPL协议，详情见LICENSE文件。