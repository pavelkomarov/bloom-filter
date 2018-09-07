% read in the data
M = csvread('output4x.csv');

%plot lnFPrate vs log10(n), 1 curve for all datapoints with a given c
M = sortrows(M, 2);% sort by c
M(:,4) = exp(M(:,4)); %convert back from log space

figure();
set(gca, 'XScale', 'log', 'YScale', 'log');
title('Log-log');
ylabel('FP rate');
xlabel('n');
hold on;

colors = ['r', 'y', 'g', 'c', 'b', 'm', 'k'];
ns = [10, 100, 1000, 10000, 100000, 1000000];%invariant, so reuse
cs = [1, 2, 5, 10, 25, 50, 100];

%% theoretical
% Pr(false positive for y not in added set) = (1 - exp(-mk/n))^k
% ideal k (which I'm using) is n/m*ln2 -> Pr(FP) = (1/2)^k
fpth = (1/2).^(log(2).*[1 2 5 10 25 50 100]);

hands = [];
for i = 1:length(cs)
	c = cs(i);
	fpavs = [];
	for n = ns
		rows = find(M(:,1)==n & M(:,2)==c);
		fpavs = [fpavs mean(M(rows,4))];
	end
	hands = [hands plot(ns, fpavs, colors(i))];
	hands = [hands plot(ns, repmat(fpth(i), [length(ns) 1]), [colors(i) '--'])];%plot theoretical in dashed line
end
legend([hands(1:2:end) hands(end)], {'c=1','c=2','c=5','c=10','c=25','c=50','c=100', 'theoretical'});

for i = 1:length(cs)
	c = cs(i);
	rows = find(M(:,2)==c);
	plot(M(rows,1), M(rows,4), [colors(i) '*']);
end

