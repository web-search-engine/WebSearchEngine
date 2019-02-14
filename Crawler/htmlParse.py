from html.parser import HTMLParser

class MyParser(HTMLParser):
	def __init__(self):
		HTMLParser.__init__(self)   
		self.attrs = []
	def handle_starttag(self, tag, attrs):
		if tag == 'a':
			for name, value in attrs:
				self.attrs.append((name,value))