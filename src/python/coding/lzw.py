def escape(s):
    ret = ""
    for c in s:
        if c == '_':
            ret += "\\_"
        else:
            ret += c
    return ret
    

def binary(n, b):
    ret = ""
    while n > 0:
        ret += str(n & 1)
        n >>= 1
    while len(ret) < b:
        ret += "0"
    return ret[::-1]
    
def log(n):
    ret = 0
    while n > 0:
        ret += 1
        n >>= 1
    return ret
    
line = input()
d = {}
for i in range(256):
    d["" + chr(i)] = i
c = 256
s = ""
allLen = 0
i = 0
while i < len(line):
    j = i + 1
    while j < len(line) and line[i:j + 1] in d:
        j += 1
    m = d[line[i:j]]
    s += " \\hline "
    b = 8 if i == 0 else log(len(d) - 1)
    bi = binary(m, b)
    s += escape(line[i:j]) + "&" + str(m) + "&" + bi + "&" + str(len(bi)) + "\\\\ "
    allLen += len(bi)
    if j != len(line):
        d[line[i:j+1]] = len(d)
    i = j    
    
s += " \\hline "

print(s)
print(allLen)
