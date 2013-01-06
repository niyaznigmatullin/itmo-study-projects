
while true
do
    python gen.py > input
    time ./ss < input > out1
    time ./suftree < input > out2
    diff out1 out2 || exit
done
