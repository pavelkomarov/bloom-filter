
% read in the data
M = csvread('thing.csv');

% plot the data itself and a histogram of what buckets
% have in them. The data should look like noise centered
% around 1000, which means the histogram should be a
% gaussian centered around 1000. That is: the number of
% buckets hit approximately the expected number of times
% is higher than those with more or fewer hits.
for k = [8:12]
	figure(2*k -1);
	plot(M(:,k));
	
	figure(2*k);
	histogram(M(:,k), 50);
end




