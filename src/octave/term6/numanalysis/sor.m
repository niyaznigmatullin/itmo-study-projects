function [x, countIterations] = sor(A, b, w, eps)
    n = size(A, 1);
    x = zeros(n, 1);
    z = diag(A);
    A = A - (eye(n) .* A);
    A = [-A b];
    A = A ./ (z * ones(1, n + 1));
    
    
    good = 1;

    x = [x; 1];
    countIterations = 0;
    norms = [];
    while (good)
        countIterations = countIterations + 1;
        old = x;
        xn = [A * x; 1];
        for i = 1 : n
            x(i) = (1 - w) * x(i) + w * (A(i, :) * x);
        end;
        norms = [norms norm(x - old)];
        if (countIterations > 20000)
            break;
        end;
        good = norm(x - old) >= eps;
    end;
    it = [1:1:countIterations];
    plot(it, norms);
    x = x(1:n);
end
