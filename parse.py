import sys
import re
import urllib
from lxml import html

def main(url):
	print url
	dirty_html = urllib.urlopen(url)
	html_code = dirty_html.read()
	tree_html = html.fromstring(html_code)

	names =  tree_html.xpath('//div[@class = "name"]/a/text()')
	# descriptions = tree_html.xpath('//div[@class = "tab-description"]/text()')
	price = tree_html.xpath('//div[@class = "price"]/text()')
	cart_id = re.findall('addToCart\(\'(\w+)\'\)', html_code)

	result = []

	for i in range(len(names)):
		result.append([names[i], price[i], cart_id[i]])

	for i in result:
		print "%s__%s__ %s" % (i[0], i[1], i[2])


if __name__ == "__main__":
	if len (sys.argv) > 1:
		main(sys.argv[1])
	else:
		print "use 'python %s [url]'" % (sys.argv[0])