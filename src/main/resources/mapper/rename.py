import os
import re

for p in os.listdir():
    if re.fullmatch("^.*Mapping.xml$", p):
        num = re.fullmatch("^.*Mapping.xml$", p).end()
        print(p[0: num-11])
        des = p[0: num-11] + "Mapper.xml"
        os.rename(p,des)


