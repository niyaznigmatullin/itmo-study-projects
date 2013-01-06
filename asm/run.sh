yasm -f elf32 -o "$1.o" "$1.asm"
ld -e main -o "$1" "$1.o"
