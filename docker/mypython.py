f = open("demofile.txt", "r")
for x in f :
    if x!="." or x!="," or x!="! " or x!="?" or x!="a"or x!="e"or x!="i" or x!="o" or x!="u"
        print(x)
f.close()