function [x, xs, norms] = biconjugateGradient(A, b, eps, maxIterations)
    n = size(A, 1);
    x1 = zeros(n, 1);
    At = A';
    r1 = b - A * x1;
    r2 = b - At * x1; 
    x2 = x1;
    p1 = r1;
    p2 = r1;
    n1 = [];
    n2 = [];
    xs = [x1];
    norms = [];
    while (norm(r1) > eps && norm(r2) > eps && size(xs) < maxIterations)
        norms = [norms; min(norm(r1), norm(r2))];
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
        if (norm(r1) < norm(r2))
            xs = [xs; x1];
        else
            xs = [xs; x2];
        end;
        n1 = [n1; norm(r1)];
        n2 = [n2; norm(r2)];
        size(n1)
        x1
    end;
    norms = [norms; norms(:, end)];
    x = xs(:,end);
end
