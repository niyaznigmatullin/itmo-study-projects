#include <cstdio>
#include <memory.h>

const int MAXN = 1555555;
const int MAXM = 2 * MAXN + 1;
const int ALPHABET = 256;
const int MAX_VALUE = ~(1 << 31);

int s[MAXN], sa[MAXN];
int parent[MAXM], start[MAXM], depth[MAXM], suf_link[MAXM], q[MAXM], links[ALPHABET];
int letter[MAXM], linkto[MAXM], head[MAXM], nextl[MAXM];
int n;
int alone, cur, left, free, freel;
bool created;

int get_link(int v, int c) {
    for (int e = head[v]; ; e = nextl[e]) {
        int ch = e < 0 ? MAX_VALUE : letter[e];
        if (ch > c) {
            return -1; 
        } else if (ch == c) {
            return linkto[e];
        }
    }
}

void set_link(int v, int c, int u) {
    int last = -1;
    for (int e = head[v]; ; e = nextl[e]) {
        int ch = e < 0 ? MAX_VALUE : letter[e];
        if (ch > c) {
            if (last < 0) {
                head[v] = freel;
            } else {
                nextl[last] = freel;
            }
            nextl[freel] = e;
            linkto[freel] = u;
            letter[freel] = c;
            freel++;
            return;
        } else if (ch == c) {
            linkto[e] = u;
            return;
        }
        last = e;
    }
}

int new_node(int p, int d, int st) {
    start[free] = st;
    parent[free] = p;
    depth[free] = d;
    return free++;
}

void go_down(int right) {
    if (depth[cur] != right - left) {
        int len = right - left - depth[parent[cur]];
        if (s[right] != s[start[cur] + len]) {
            int u = new_node(parent[cur], right - left, start[cur]);
            set_link(parent[u], s[start[cur]], u);
            set_link(u, s[start[cur] + len], cur);
            start[cur] += len;
            parent[cur] = u;
            created = true;
            cur = u;
        }
    }
    if (depth[cur] == right - left) {
        int z = get_link(cur, s[right]);
        if (z < 0) {
            z = new_node(cur, -1, right);
            set_link(cur, s[right], z);
            created = true;
        }
        cur = z;
    }
}

void buildTree() {
    alone = -1;
    free = 0;
    cur = new_node(0, 0, 0);
    left = 0;
    memset(suf_link, -1, sizeof suf_link);
    memset(head, -1, sizeof head);
    suf_link[cur] = cur;
    for (int right = 0; right < n; right++) {
        while (left <= right) {
            created = false;
            go_down(right);
            if (alone >= 0) {
                suf_link[alone] = parent[cur];
                alone = -1;
            }
            if (!created) break;
            cur = parent[cur];
            if (suf_link[cur] < 0) {
                alone = cur;
                cur = parent[cur];
            }
            cur = suf_link[cur];
            ++left;
            while (depth[cur] >= 0 && depth[cur] < right - left) {
                go_down(left + depth[cur]);
            }
        }
    }
}

void buildSA() {
    int headstack = 0;
    q[headstack++] = 0;
    int cnt = 0;
    while (headstack > 0) {
        int v = q[--headstack];
        if (depth[v] < 0) {
            sa[cnt++] = start[v] - depth[parent[v]];
        }
        int curz = 0;
        for (int e = head[v]; e >= 0; e = nextl[e]) {
            links[curz++] = linkto[e];
        }
        for (int i = curz - 1; i >= 0; i--) {
            int u = links[i];
            if (u < 0) continue;
            q[headstack++] = u;
        }
    }
}

int main() {
    int c;
    do {
        c = getchar();
    } while (c < 'a' || c > 'z');
    n = 0;
    while (c >= 'a' && c <= 'z') {
        s[n++] = c - 'a' + 1;
        c = getchar();
    }
    s[n++] = 0;
    buildTree();
    return 0;
    buildSA();
    for (int i = 0; i < n; i++) {
        printf("%d%c", sa[i], " \n"[i == n - 1]);
    }
}
