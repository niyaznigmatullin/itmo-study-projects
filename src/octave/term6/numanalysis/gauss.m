function [x] = gauss(A, b)
    n = size(A);
    C = A;
    A = [A b];
    id = [1:n];
    for i = 1 : n
        for j = i + 1 : n
            if (abs(A(i, i)) < abs(A(j, i)))
                t = A(i);
                A(i) = A(j);
                A(j) = t;
                t = id(i);
                id(i) = id(j);
                id(j) = t;
            end;
        end;
        for j = (i + 1) : n
            A(j, :) = A(j, :) - A(i, :) * (A(j, i) / A(i, i));
        end;
    end;
    for i = n : -1 : 1
        for j = i - 1 : -1 : 1
            A(j, :) = A(j, :) - A(i, :) * (A(j, i) / A(i, i));
        end;
    end;
    c = A(:, n + 1:n + 1);
    A = A(:, 1:n);
    ans = c ./ diag(A);
    x = zeros(n, 1);
    for i = 1 : n
        x(id(i)) = ans(i);
    end;
end
