
def escape(s):
    ret = ""
    for c in s:
        if c == '_':
            ret += "\\_"
        else:
            ret += c
    return ret
    
def binary(n):
    if n == 0:
        return "0"
    ret = ""
    while n > 0:
        ret += str(n & 1)
        n >>= 1
    return ret[::-1]
        
def omega(n):
    s = "0"
    while n > 1:
        t = binary(n)
        s = t + s
        n = len(t) - 1
    return s

line = input()
w = len(line)
bits = 0
while w >> bits != 0:
    bits += 1
win = "$"*w + line
s = ""
allLen = 0
i = 0
while i < len(line):
    c = line[i]
    best = 0
    where = 0
    for j in range(1, len(line) - i + 1):
        if i + j > len(line):
            break
        t = line[i:i+j]
        if win.find(t) < w:
            best = j
            where = win.find(t)
    s += " \\hline "
    if best > 0:
        z1 = binary(w - where)
        z2 = omega(best)
        while len(z1) < bits:
            z1 = "0" + z1
        s += " 1&" + escape(line[i:i+best]) + "&" + str(w - where) + "&" + str(best) + "&" + "1 " + z1 + " " + z2 + "&" + str(1 + len(z1) + len(z2)) + "\\\\ "
        allLen += 1 + len(z1) + len(z2)
        i += best
        win = win[best:]
    else:
        z = binary(ord(c))
        while len(z) < 8:
            z = "0" + z
        s += " 0&" + escape(str(c)) + "&" + "---" + "&" + "---" + "&" + "0 " + z + "&9" + "\\\\ " 
        allLen += 9
        i += 1
        win = win[1:]
s += " \\hline "
print(s)
print(allLen)