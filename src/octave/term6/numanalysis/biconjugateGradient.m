function [x, countIterations] = biconjugateGradient(A, b, eps)
    n = size(A, 1);
    x1 = zeros(n, 1);
    At = A';
    r1 = b - A * x1;
    r2 = b - At * x1; 
    x2 = x1;
    p1 = r1;
    p2 = r1;
    countIterations = 0;
    n1 = [];
    n2 = [];
    while (norm(r1) > eps && norm(r2) > eps)
        countIterations = countIterations + 1;
        alpha = r2' * r1 / (p2' * A * p1); 
        x1 = x1 + alpha * p1;
        x2 = x2 + alpha * p2;
        nr1 = r1 - alpha * A * p1;
        nr2 = r2 - alpha * At * p2;
        beta = nr2' * nr1 / (r2' * r1);
        p1 = nr1 + beta * p1;
        p2 = nr2 + beta * p2;
        r1 = nr1;
        r2 = nr2;
        n1 = [n1 norm(r1)];
        n2 = [n2 norm(r2)];
    end;
    z = [1:1:countIterations];
    plot(z, n1, "1");
    if (norm(r1) < norm(r2))
        x = x1;
    else
        x = x2;
    end;
end
