%define bufsize 1000
section	.text
    global main			;must be declared for linker (ld)
    global readUInt32
    global readChar
    global readBuffer
    global _init

main:					;tell linker entry point
;    xor eax, eax
    call _init
    call readUInt32
    mov [num], eax
    xor ecx, ecx
    mov [sum], ecx
    loop3:
    call readUInt32
    mov ebx, [sum]
    add ebx, eax
    mov [sum], ebx
    mov ecx, [num]
    dec ecx
    mov [num], ecx
    cmp ecx, 0    
    jne loop3
;    call readToBuffer
;    mov eax, ebx
;    call readUInt32
    xor edx, edx
    mov [cntbuf], edx
    mov eax, [sum]
    loop4:
    mov ebx, [cntbuf]
    mov ecx, 10
    xor edx, edx
    div ecx
    add edx, '0'
    mov [buffer + ebx], edx
    inc ebx
    mov [cntbuf], ebx
    cmp eax, 0
    jnz loop4
    mov eax, 4
    mov ebx, 1
    mov ecx, buffer
    mov edx, [cntbuf]
    int 0x80
	mov	eax, 1	;system call number (sys_exit)
	int	0x80	;call kernel

_init:
    mov eax, 0
    mov [current], eax
    mov [cntbuf], eax 
    ret

readUInt32:
    loop1:
    call readChar
    mov ebx, eax
    cmp eax, 0
    jl bad
    call isNum
    cmp eax, 0
    je loop1
    xor ecx, ecx
    mov [sum2], ecx
    loop2:
    mov ecx, [sum2]
    mov eax, ecx
    mov edx, 10
    mul edx 
    mov ecx, eax
    sub ebx, '0'
    add ecx, ebx
    mov [sum2], ecx
    call readChar
    mov ebx, eax
    call isNum
    cmp eax, 0
    jne loop2
    mov eax, [sum2]
    ret
    bad:
    ud2
    mov eax, 1
    int 0x80
    ret

isNum:
    cmp eax, '0'
    jl num1
    cmp eax, '9'
    jg num1
    mov eax, 1
    ret
    num1:
    xor eax, eax
    ret

isNumDash:
    cmp eax, '0'
    jl ind1
    cmp eax, '9'
    jg ind1
    mov eax, 1
    ret
    ind1:
    cmp eax, '-'
    jne ind2
    mov eax, 1
    ret
    ind2:
    xor eax, eax
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
    cntbuf:     resd    1   ; buffercnt
    current:    resd    1   ; bufferread
    num:        resd    1   ; n
    sum:        resd    1   ; sum
    sum2:       resd    1   ; sum2
