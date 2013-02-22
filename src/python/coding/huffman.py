from fractions import Fraction

class Node:
    
    def __init__(self, left, right, leaf, prob):
        self.left = left
        self.right = right
        self.leaf = leaf
        self.prob = prob


def go(v, code, s):
    if v.leaf >= 0:
        code[chr(v.leaf)] = s
        return
    go(v.left, code, s + "0")
    go(v.right, code, s + "1")
 
line = input()

freq = {}
for c in line:
    if c in freq:
        freq[c] += 1
    else:
        freq[c] = 1

nodes = []
for c in freq.keys():
    nodes.append(Node(None, None, ord(c), Fraction(freq[c], len(line))))

while len(nodes) > 1:
    nodes.sort(key = lambda node: node.prob)
    a, b = nodes[0], nodes[1]
    nodes = nodes[2:]
    nodes.append(Node(a, b, -1, a.prob + b.prob))

code = {}
go(nodes[0], code, "")

letters = []
for c in freq.keys():
    letters.append(c)
letters.sort(key = lambda c: code[c])
first = ""
first += "\\hline "
for c in letters:
    first += " " + ("\\_" if c == '_' else str(c)) + "&" + str(freq[c]) + "&" + str(len(code[c])) + "&" + code[c] + "\\\\ "
    first += " \\hline"

second = ""
second += ("\\hline ")

curLen = 0
for c in line:
    curLen += len(code[c])
    z = Fraction(freq[c], len(line))
    second += (" " + ("\\_" if c == '_' else str(c)) + "&" + str(z) + "&" + str(len(code[c])) + "&" + code[c] + "&" + str(curLen) + "\\\\ ")
    second += (" \\hline ")    

print(first)
print(second)