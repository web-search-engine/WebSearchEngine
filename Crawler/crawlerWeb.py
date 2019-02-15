from flask import Flask, render_template,request,url_for,redirect
import os
import crawler.crawler as crawler
import getTopResult.googleUrlLink as googleUrlLink
import crawler.crawlerExecution as crawlerExecution

curPath = os.path.join(os.getcwd())
print(curPath)
app = Flask(__name__,template_folder = curPath+"/template", static_folder = curPath+"/css" )


@app.route("/", methods=['GET','POST'])
def index():
	if request.method == 'GET':
		return render_template('crawlerWeb.html')
	else:
		keyWord = request.form["keyWords"]
		N = request.form["Number"]
		iniLinks = googleUrlLink.search(keyWord)
		crawl = crawler.crawler(iniLinks, int(N))
		crawlerExecution.crawlerExecution(crawl)

		print ("Search " + keyWord + " get " + N + " results!!!")
		return redirect("/")

if __name__ == '__main__':
    app.run(debug=True,host='localhost',port=8081)