
def binary(n):
    if n == 0:
        return "0"
    ret = ""
    while n > 0:
        ret += str(n & 1)
        n >>= 1
    return ret[::-1]
        



def fact(n):
    ret = 1
    for i in range(2, n + 1):
        ret *= i
    return ret

def choose(n, k):
    return fact(n) // fact(k) // fact(n - k)
        
line = input()
n = len(line)
print("n = " + str(n))
f = {}
for c in line:
    if c in f:  
        f[c] += 1
    else:
        f[c] = 1

n1 = choose(n + 255, 255)

print("n1 = " + str(n1))
b = fact(n)
for i in f.values():
    b //= fact(i)
n2 = b
print("n2 = " + str(n2))

print(len(binary(n1)))
print(len(binary(n2)))

s = ""
for c in sorted(f.keys(), key = lambda c: ord(c)):
    s += " \\hline "
    s += ("\\_" if c == '_' else str(c)) + "&" + str(ord(c)) + "&" + str(f[c]) + "\\\\ "
s += " \\hline "
print(s)