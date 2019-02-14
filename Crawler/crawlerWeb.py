from flask import Flask, render_template,request,url_for,redirect
import os

curPath = os.path.join(os.getcwd())
app = Flask(__name__,template_folder = curPath, static_folder = curPath+"\\css" )


@app.route("/", methods=['GET','POST'])
def index():
	if request.method == 'GET':
		return render_template('crawlerWeb.html')
	else:
		keyWord = request.form["keyWords"]
		N = request.form["Number"]
		print ("Search " + keyWord + " get " + N + " results!!!")
		return redirect("/")



if __name__ == '__main__':
    app.run(debug=True,host='localhost',port=8080)