# WebSearchEngine

## Description
This project implements a Web Search Engine.  

It is develops in 3 steps:
- [x] Build a web links crawler
- [x] Build index for web links
- [ ] Develop query algorithm

## Steps
This is the first stage of the project.
1. Using input the keywords and number of pages(X) want to crawler
2. Initialize the crawler with top 10 links in Google using Google API
3. Using PageRank algorithm to sort the links
4. Give back the Top X pages


## Instructions To Run
**1. Install Python 3**

**2. Clone the project into "/your/path"**  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*git clone ...*  

**3. Go into the src directory and run it**  
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;*python crawlerWeb.py*

## Notice
In the crawlerWeb.py file
1. Mac user use 
app = Flask(__name__,template_folder = curPath+"/template", static_folder = curPath+"/css" )
2. Windows user use
app = Flask(__name__,template_folder = curPath+"\\template", static_folder = curPath+"\\css" )

## Project Structure
```bash
├── README.md
└───Crawler
    ├───crawler
    │   ├── *crawlerExecution.py
    │   └── *crawler.py
    ├───css
    ├───getTopResult
    │   └── *googleUrlLink.py
    ├───htmlParse
    │   └── *htmlParse.py
    ├───*crawlerWeb.py
    └───template   
```


## Team
- Kuang Sheng
- Xinyu Ma
