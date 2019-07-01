package com.learn;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Solution {
	public List<String> findRepeatedDnaSequences(String s) {
		long ss = System.currentTimeMillis();
		List<String> list = new ArrayList<>();
		if (s == null || s.length() < 11) {
			return list;
		}
		Map<String, Integer> map = new HashMap<>();
		int length = s.length();
		for (int i = 0; i < length; i++) {
			try {
				String str = s.substring(i, i + 10);
				if (map.containsKey(str)) {
					int times = map.get(str);
					map.put(str, times + 1);
					if (times == 2) {
						list.add(str);
					}
				} else {
					map.put(str, 1);
				}
			} catch (Exception e) {

			}

		}
		long e = System.currentTimeMillis();
		System.out.println(e - ss);
		return list;
		// write your code here
	}

	public String getPermutation(int n, int k) {
		Set<Integer> nums = new TreeSet<>();
		for (int i = n; i > 0; i--) {
			nums.add(i);
		}
		nums.forEach((item) -> {
			System.out.print(item);
		});
		StringBuffer sb = new StringBuffer();
		int c = 0;
		int tc = 0;
		for (int i = 1; nums.size() > 0 && i <= n;) {
			c = getC(nums.size() - 1);
			if (tc != k) {
				tc += c;
			} else {
				i++;
			}
			if (i > 3) {
				i = 1;
			}
			if (tc < k) {
				i++;
			} else if (tc == k) {
				if (nums.contains(i)) {
					sb.append(i);
					nums.remove(i);
				}
			} else {
				tc -= c;
				if (nums.contains(i)) {
					sb.append(i);
					nums.remove(i);
				}
			}
		}
		return sb.toString();
	}

	public int getC(int n) {
		int result = 1;
		for (int i = 1; i <= n; i++) {
			result *= i;
		}
		return result;
	}

	public int getAnswer(int[] a) {
		int max = 0;
		int n = a.length;
		for (int i = 0; i < n; i++) {
			if (a[i] % 2 == 1) {
				for (int j = i + 1; j < n; j++) {
					if (a[j] % 2 == 0 && a[i] < a[j]) {
						int c = a[j] - a[i];
						if (c > max) {
							max = c;
						}

					}
				}
			}
		}
		return max;
		// Write your code here
	}

	/**
	 * 139. 最接近零的子数组和
	
		 给定一个整数数组，找到一个和最接近于零的子数组。返回第一个和最右一个指数。你的代码应该返回满足要求的子数组的起始位置和结束位置
		
		样例
		给出[-3, 1, 1, -3, 5]，返回[0, 2]，[1, 3]， [1, 1]， [2, 2] 或者 [0, 4]。
		
		挑战
		O(nlogn)的时间复杂度
	 * @param nums
	 * @return
	 */
	public int[] subarraySumClosest(int[] nums) {
		// write your code here
		int[] results = new int[2];
		if (nums == null || nums.length == 0) {
			return new int[] {};
		}
		if (nums.length == 1) {
			return new int[] { 0, 0 };
		}
		Map<Integer, Integer> map = new HashMap<>();
		int[] prefixSum = new int[nums.length + 1];
		int sum = 0;
		map.put(0, -1);
		prefixSum[0] = 0;
		for (int i = 0; i < nums.length; i++) {
			sum += nums[i];
			if (map.containsKey(sum)) {
				results[0] = map.get(sum) + 1;
				results[1] = i;
				return results;
			}
			map.put(sum, i);
			prefixSum[i + 1] = sum;
		}
		Arrays.sort(prefixSum);
		int minDiff = Integer.MAX_VALUE;
		int left = 0;
		int right = 0;
		for (int i = 0; i < prefixSum.length - 1; i++) {
			int a = Math.abs(prefixSum[i] - prefixSum[i + 1]);
			if (minDiff > a) {
				minDiff = a;
				left = prefixSum[i];
				right = prefixSum[i + 1];
			}
		}
		Integer leftI = map.get(left);
		Integer rightJ = map.get(right);
		if (leftI < rightJ) {
			results[0] = leftI + 1;
			results[1] = rightJ;
		} else {
			results[0] = rightJ + 1;
			results[1] = leftI;
		}
		return results;
	}

	public String encode(List<String> strs) {
		// write your code here
		StringBuilder sb = new StringBuilder();
		for (String s : strs) {
			for (char c : s.toCharArray()) {
				if (c == ';') {
					sb.append(";");
				}
				sb.append(c);
			}

			sb.append(";:");
		}

		return sb.toString();
	}

	/*
	 * @param str: A string
	 * @return: dcodes a single string to a list of strings
	 */
	public List<String> decode(String str) {
		// write your code here
		List<String> list = new ArrayList();
		char[] sc = str.toCharArray();
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while (i < sc.length) {
			if (sc[i] == ';') {
				if (sc[i + 1] == ':') {
					list.add(sb.toString());
					sb = new StringBuilder();
				} else {
					sb.append(sc[i + 1]);
				}

				i += 2;
			} else {
				sb.append(sc[i]);
				i++;
			}
		}

		return list;

	}

	public int maximumSwap(int num) {
		// Write your code here
		String s = Integer.toString(num);
		if (s.length() < 2) {
			return num;
		}
		char[] cs = s.toCharArray();
		int[] is = new int[cs.length];
		HashMap<Integer, HashSet<Integer>> map = new HashMap<Integer, HashSet<Integer>>();
		for (int i = 0; i < cs.length; i++) {
			HashSet<Integer> set = null;
			is[i] = Integer.parseInt(Character.toString(cs[i]));
			if (map.containsKey(is[i])) {
				set = map.get(is[i]);
				set.add(i);
			} else {
				set = new HashSet<Integer>();
				set.add(i);
				map.put(is[i], set);
			}
		}
		int[] ois = Arrays.copyOf(is, is.length);
		Arrays.sort(is);
		int n = is.length - 1;
		boolean has = false;
		int first = -1;
		int sencond = -1;
		for (int i = 0; i < is.length; i++) {
			if (ois[i] < is[n - i]) {
				first = i;
				sencond = map.get(is[n - i]).iterator().next();
				break;
			}
		}
		StringBuilder sb = new StringBuilder();
		int temp = ois[sencond];
		ois[sencond] = ois[first];
		ois[first] = temp;
		for (int i : ois) {
			sb.append(i);
		}
		return Integer.parseInt(sb.toString());
	}

	public static int KMP(String str, String sub) {
		int i = 0, j = -1;
		int[] next = nextIndex(sub);
		while (i < str.length() && j < sub.length()) {
			if (j == -1 || str.charAt(i) == sub.charAt(j)) {
				i++;
				j++;
			} else {
				j = next[j];
			}
		}
		if (j == sub.length()) {
			return i - j;
		} else {
			return -1;
		}
	}

	/**
	 * KMP的next数组
	 * @param str
	 * @return
	 */
	public static int[] nextIndex(String str) {
		int[] next = new int[str.length() + 1];
		next[0] = -1;
		int k = -1;
		int j = 0;
		while (j < str.length() - 1) {
			// DABD
			if (k == -1 || str.charAt(k) == str.charAt(j)) {
				//1
				//2
				++j;
				++k;
				if(str.charAt(k) != str.charAt(j)) {
					next[j] = k;//1 end
				}else {
					next[j] = next[k];
				}//2 end
			} else {
				k = next[k];
			}
		}

		return next;
	}
	public static void count(int len) {
		int num2 = 0;
		if(len>7) {
			num2 += 7*(len-3);
		}else {
			num2 += len*(len+1)/2;
		}
		System.out.println(len + " " +num2);
	}
	public static void count(String s) {
		int len = s.length();
		int num = 0;
		for(int i=1;i<=len;i++) {
			int j = len - i + 1;
			if(j>7) {
				num += 7*(7-1)/2;
			}else {
				if(j==1) {
					num++;
					System.out.println(s.substring(i));
				}else {
					num += j;
					for(int k=0;k<j;k++) {
						System.out.println(s.substring(i, k));
					}
				}
			}
		}
		System.out.println(len + " " +num);
	}
/*	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Solution s = new Solution();
		//		FileReader reader = new FileReStrader("C:\\Users\\esensoft\\Downloads\\107.in");
		//		BufferedReader br = new BufferedReader(reader);
		//		System.out.println(s.findRepeatedDnaSequences(""));
		//		System.out.println(s.getPermutation(3, 4));
		//		System.out.println(s.getAnswer(new int[] {32,8,54,62,63,99,32,24,36,87}));
		//		System.out.println(Arrays.toString(s.subarraySumClosest(new int[] { 1, 1, 1, 1, 1, 1, 1, 1, -9 })));
		//		ArrayList<String> strs = new ArrayList<>();
		//		strs.add("lint;:s");
		//		strs.add("code");
		//		System.out.println(s.decode(s.encode(strs)));
		//		System.out.println(s.maximumSwap(2736));
//		System.out.println(KMP("DABCDABDE","DABD"));
//		List<Integer> lsit = new ArrayList<>();
//		lsit.add(1);
//		lsit.add(2);
//		lsit.add(3);
//		lsit.add(1,100);
//		System.out.println(lsit);
		for (int i=1;i<=20;i++) {
			count(i);
		}
//		count("处置固定资产、无形资产和其他长期资产收回的现金净额");
	}*/
}
