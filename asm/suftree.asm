%define bufsize 100000
%define maxstrlen 1555555
%define maxtreesize maxstrlen * 2 + 1

section	.text
    global main			;must be declared for linker (ld)
    global readChar
    global readBuffer
    global printBuf
    global _init
    global getLink
    global setLink
    global putChar
    global putInt
    global addOne
    global buildTree
    global buildSA
    global newNode
    global goDown
    global addv
    global addl
main:					;tell linker entry point
    call _init
    mov [strlen], dword 0
    mainloop:
        call readChar
        cmp eax, 32
        jle endmainloop
        mov ebx, [strlen]
        mov [astr + ebx], al
        inc ebx
        mov [strlen], ebx
    jmp mainloop
    endmainloop:
    mov ebx, [strlen]
    mov [astr + ebx], byte 0
    inc ebx;
    mov [strlen], ebx
    call buildTree
    mov eax, 1
    int 0x80
    call buildSA
    xor eax, eax
    mov [index], eax
    mainloop2:
    mov eax, [index]
    push dword [sa + eax * 4]
    inc eax
    mov [index], eax
    call putInt
    mov eax, [index]
    cmp eax, [strlen]
    je endmainloop2
    push dword 32
    call putChar
    jmp mainloop2
    endmainloop2:
    push dword 10
    call putChar
    call printBuf
	mov	eax, 1	;system call number (sys_exit)
	int	0x80	;call kernel


getLink:
    mov eax, [esp + 4]
    mov eax, [head + eax * 4]
    getLinkLoop1:
        test eax, eax        
        js getLetter1
        xor ebx, ebx
        mov bl, [letter + eax]
        jmp getNLetter1
        getLetter1:
        mov ebx, 256
        getNLetter1:
        cmp ebx, [esp + 8]
        jg nolink
        je foundlink
        mov eax, [nextl + eax * 4]
    jmp getLinkLoop1
    nolink:
        mov eax, -1
        ret 8
    foundlink:
        mov eax, [linkto + eax * 4]
        ret 8


setLink:
    mov eax, [esp + 4]
    mov eax, [head + eax * 4]
    mov ecx, -1
    setLinkLoop1:
        test eax, eax
        js getLetter2
        xor ebx, ebx
        mov bl, [letter + eax]
        jmp getNLetter2
        getLetter2:
        mov ebx, 256
        getNLetter2:
        cmp ebx, [esp + 8]
        jg setNewLink
        je setOldLink
        mov ecx, eax
        mov eax, [nextl + eax * 4]
    jmp setLinkLoop1
    setOldLink:
        mov ebx, [esp + 12]
        mov [linkto + eax * 4], ebx
        ret 12
    setNewLink:
        mov ebx, [freel]
        test ecx, ecx
        js firstlink
        mov [nextl + ecx * 4], ebx
        jmp endnofirst
        firstlink:
        mov edx, [esp + 4]
        mov [head + edx * 4], ebx
        endnofirst:
        mov [nextl + ebx * 4], eax
        mov eax, [esp + 12]
        mov [linkto + ebx * 4], eax
        mov eax, [esp + 8]
        mov [letter + ebx], al
        inc ebx
        mov [freel], ebx
        ret 12
newNode:
    mov eax, [free]
    mov ebx, [esp + 4]
    mov [parent + eax * 4], ebx
    mov ebx, [esp + 8]
    mov [depth + eax * 4], ebx
    mov ebx, [esp + 12]
    mov [start + eax * 4], ebx
    mov ebx, eax
    inc ebx
    mov [free], ebx
    ret 12

goDown:
    mov eax, [cur]
    mov eax, [depth + eax * 4]
    mov ebx, [right]
    sub ebx, [left]
    cmp eax, ebx
    je noaddv
    call addv
    noaddv:
    mov eax, [cur]
    mov eax, [depth + eax * 4]
    mov ebx, [right]
    sub ebx, [left]
    cmp eax, ebx
    jne noaddl
    call addl
    noaddl:
    ret

addv:
    mov eax, [right]
    sub eax, [left]
    mov ebx, [cur]
    mov ebx, [parent + ebx * 4]
    mov ebx, [depth + ebx * 4]
    sub eax, ebx
    mov ebx, [cur]
    mov ebx, [start + ebx * 4]
    add ebx, eax
    mov [addvlen], eax
    xor ecx, ecx
    mov cl, [astr + ebx]
    mov edx, [right]
    cmp cl, [astr + edx]
    je endaddv
        mov eax, [cur]
        push dword [start + eax * 4]
        mov ebx, [right]
        sub ebx, [left]
        push dword ebx
        push dword [parent + eax * 4]
        call newNode
        mov [unode], eax
        push dword [cur]
        push ecx
        push eax
        call setLink
        push dword [unode]
        mov ebx, [cur]
        mov ebx, [start + ebx * 4]
        xor ecx, ecx
        mov cl, [astr + ebx]
        push ecx
        mov eax, [cur]
        mov eax, [parent + eax * 4]
        push eax
        call setLink
        mov eax, [cur]
        mov eax, [start + eax * 4]
        add eax, [addvlen]
        mov ebx, [cur]
        mov [start + ebx * 4], eax
        mov eax, [unode]
        mov [parent + ebx * 4], eax
        xor ebx, ebx
        mov ebx, 1
        mov [created], bl
        mov [cur], eax
    endaddv:
    ret

addl:
    mov eax, [right]
    xor ebx, ebx
    mov bl, [astr + eax]
    push ebx
    push dword [cur]
    call getLink
    test eax, eax
    jns endaddl
    push dword [right]
    push dword -1
    push dword [cur]
    call newNode
    push eax
    mov ebx, [right]
    xor ecx, ecx
    mov cl, [astr + ebx]
    push ecx
    push dword [cur]
    mov [cur], eax
    call setLink
    xor ebx, ebx
    mov ebx, 1
    mov [created], bl
    ret
    endaddl:
    mov [cur], eax
    ret


buildTree:
    mov eax, suflink
    mov ebx, [strlen]
    add ebx, ebx
    inc ebx
    add ebx, ebx
    add ebx, ebx
    add ebx, eax
    buildloop2:
    mov [eax], dword -1
    lea eax, [eax + 4]
    cmp eax, ebx
    jne buildloop2
    mov eax, head
    mov ebx, [strlen]
    add ebx, ebx
    inc ebx
    add ebx, ebx
    add ebx, ebx
    add ebx, eax
    buildloop3:
    mov [eax], dword -1
    lea eax, [eax + 4]
    cmp eax, ebx
    jne buildloop3
    mov [alone], dword -1
    mov [free], dword 0
    mov [freel], dword 0
    push dword 0
    push dword 0
    push dword 0
    call newNode
    mov [cur], eax
    mov [left], dword 0
    mov [suflink + eax * 4], eax
    mov [right], dword 0
    buildloop1:
        call addOne
    mov eax, [right]
    inc eax
    mov [right], eax
    mov eax, [strlen]
    cmp [right], eax
    jne buildloop1
    ret

addOne:
    xor eax, eax
    mov [created], al
    call goDown
    mov eax, [alone]
    test eax, eax
    js noalone
    mov ebx, [cur]
    mov ebx, [parent + ebx * 4]
    mov [suflink + eax * 4], ebx
    mov [alone], dword -1
    noalone:
    mov al, [created]
    test al, al
    jz endaddone
    mov eax, [cur]
    mov eax, [parent + eax * 4]
    mov [cur], eax
    mov ebx, [suflink + eax * 4]
    test ebx, ebx
    jns noaddalone
    mov [alone], eax
    mov eax, [parent + eax * 4]
    mov [cur], eax
    noaddalone:
    mov eax, [suflink + eax * 4]
    mov [cur], eax
    mov ebx, [left]
    inc ebx
    mov [left], ebx
    addoneloop:
    mov eax, [cur]
    mov eax, [depth + eax * 4]
    test eax, eax
    js noaddoneloop
    mov ebx, [right]
    sub ebx, [left]
    cmp eax, ebx
    jge noaddoneloop
    mov ecx, [right]
    mov [right2], ecx
    add eax, [left]
    mov [right], eax
    call goDown
    mov ecx, [right2]
    mov [right], ecx
    jmp addoneloop
    noaddoneloop:    
    mov eax, [left]
    cmp eax, [right]
    jg endaddone
    jmp addOne
    endaddone:
    ret

buildSA:
    mov [qhead], dword 1
    mov [queue], dword 0
    mov [sacnt], dword 0
    saloop:
    mov eax, [qhead]
    test eax, eax
    jz endsaloop
    mov ebx, [qhead]
    dec ebx
    mov [qhead], ebx
    mov eax, [queue + ebx * 4]
    mov ecx, [depth + eax * 4]
    test ecx, ecx
    jns noprintsa
    mov ecx, [start + eax * 4]
    mov edx, [parent + eax * 4]
    mov edx, [depth + edx * 4]
    sub ecx, edx
    mov ebx, [sacnt]
    mov [sa + ebx * 4], ecx
    inc ebx
    mov [sacnt], ebx
    noprintsa:
    mov eax, [head + eax * 4]
    mov [espsave], esp
    saloop2:
        test eax, eax
        js endsaloop2
        push dword [linkto + eax * 4]
        mov eax, [nextl + eax * 4]
    jmp saloop2
    endsaloop2:
    saloop3:
    cmp esp, [espsave]
    je endsaloop3
    pop eax
    mov ebx, [qhead]
    mov [queue + ebx * 4], eax
    inc ebx
    mov [qhead], ebx
    jmp saloop3
    endsaloop3:
    jmp saloop
    endsaloop:
    ret

section .data
    unode:      resd        1

_init:
    mov eax, 0
    mov [current], eax
    mov [cntbuf], eax 
    ret

putInt:
    mov eax, [esp + 4]
    mov [espsave], esp
    test eax, eax
    jnz putnonzero
    push dword '0'
    putnonzero:
    test eax, eax
    jz endnonzero
    xor edx, edx
    mov ebx, 10
    div ebx
    add edx, '0'
    push edx
    jmp putnonzero    
    endnonzero:
    call putChar
    cmp esp, [espsave]
    jne endnonzero
    ret 4

section .data
    espsave:     resd    1   ;
    index:      resd    1   ;

putChar:
    mov eax, [cntoutbuf]
    cmp eax, bufsize
    jne putit
    call printBuf
    putit:
    mov eax, [esp + 4]
    mov ebx, [cntoutbuf]
    mov [bufferout + ebx], al
    inc ebx
    mov [cntoutbuf], ebx
    ret 4

printBuf:
    mov eax, 4
    mov ebx, 1
    mov ecx, bufferout
    mov edx, [cntoutbuf]
    int 0x80
    mov [cntoutbuf], dword 0
    ret

readChar:
    mov eax, [cntbuf]
    mov ebx, [current]
    cmp eax, ebx
    jg already    
    call readToBuffer
    cmp eax, 0
    jne already
    mov eax, -1
    ret
    already:
    mov ebx, [current]
    xor eax, eax
    mov al, [buffer + ebx]
    inc ebx
    mov [current], ebx
    ret

readToBuffer:
    mov eax, [cntbuf]
    cmp eax, 0
    jge doread
    mov eax, 0
    ud2
    ret
    doread:
    mov edx, bufsize
    mov ecx, buffer
    mov ebx, 0
    mov eax, 3
    int 0x80
    mov [cntbuf], eax
    mov ebx, 0
    mov [current], ebx
    ret
    
section .data
    buffer:     resb    bufsize ; buffer
    bufferout:  resb    bufsize ;
    cntoutbuf:  resd    1   ;
    cntbuf:     resd    1   ; buffercnt
    current:    resd    1   ; bufferread
    astr:       resb    maxstrlen ; string
    strlen:     resd    1   ;
    depth:      resd    maxtreesize ; 
    parent:     resd    maxtreesize ;
    start:      resd    maxtreesize ;
    free:       resd    1   ;
    freel:      resd    1   ;
    letter:     resb    maxtreesize ;
    left:       resd    1   ;
    right:      resd    1   ;
    right2:     resd    1   ;
    cur:        resd    1   ;
    addvlen:        resd    1   ;
    created:    resb    1   ;
    head:       resd    maxtreesize
    nextl:      resd    maxtreesize ;
    linkto:     resd    maxtreesize ;
    alone:      resd    1   ;
    suflink:    resd    maxtreesize ;
    sa:         resd    maxstrlen   ;
    queue:      resd    maxtreesize ;
    sacnt:      resd    1   ;
    qhead:      resd    1   ;
