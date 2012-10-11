function test()
    w = 0:0.05:1.5;
    w = w';
    y = [];
    A = genIllConditioned(10);
    b = randn(10, 1);
    for i = 1 : size(w)
        [trash, z] = sor(A, b, w(i), 1e-20);
        y = [y z];
        z
    end;
    y
    plot(w, y, "1");
end;
