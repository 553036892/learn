package com.learn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Main {
	public static void main(String[] args) {
		System.out.println(count(
				"This won iz correkt. It has, No Mistakes et Oll. But there are two BIG mistakes in this sentence. and here is one more."));
		//		getTheMax(getInput());
		//		computeLastDigit(Long.parseLong("7680103979767522"), Long.parseLong("85610382768811514"));
	}

	private static char[] count(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static List<Integer> getInput() {
		Scanner sc = new Scanner(System.in);
		List<Integer> sz = new ArrayList<>();
		int x = 0;
		while (true) {
			String nextLine = sc.nextLine();
			if (!"".equals(nextLine) && nextLine != null) {
				String[] split = nextLine.split(" ");
				for (int i = 0; split != null && i < split.length; i++) {
					int parseInt = Integer.parseInt(split[i]);
					if (parseInt != 0) {
						sz.add(parseInt);
					}
				}
			} else {
				break;
			}
		}
		return sz;
	}

	public static void getTheMax(List<Integer> list) {
		int maxZ1 = 0;
		int maxZ2 = 0;
		int maxZ3 = 0;
		int maxF1 = 0;
		int maxF2 = 0;
		for (int x : list) {
			if (x < 0) {
				if (x < maxF1) {
					maxF2 = maxF1;
					maxF1 = x;
				} else if (x < maxF2) {
					maxF2 = x;
				}
			} else {
				if (x > maxZ1) {
					maxZ3 = maxZ2;
					maxZ2 = maxZ1;
					maxZ1 = x;
				} else if (x > maxZ2) {
					maxZ3 = maxZ2;
					maxZ2 = x;
				} else if (x > maxZ3) {
					maxZ3 = x;
				}
			}
		}
		int max1 = maxZ1 * maxZ2 * maxZ3;
		int max2 = maxZ1 * maxF1 * maxF2;
		System.out.println(max1 > max2 ? max1 : max2);
	}

	public static int computeLastDigit(long A, long B) {
		// write your code here
		long x = 1;
		String temp = null;
		for (long i = A + 1; i <= B; i++) {
			temp = Long.toString(i);
			temp = new Character(temp.charAt(temp.length() - 1)).toString();
			x *= Integer.parseInt(temp);
			temp = Long.toString(x);
			temp = new Character(temp.charAt(temp.length() - 1)).toString();
			x = Long.parseLong(temp);
		}
		temp = Long.toString(x);
		return Integer.parseInt(new Character(temp.charAt(temp.length() - 1)).toString());
	}

	/**
	 * n位水仙花数
	 * @param n
	 * @return
	 */
	public static List<Integer> getNarcissisticNumbers(int n) {
		List<Integer> list = new ArrayList<Integer>();
		int m = (int) Math.pow(10, n);
		int i = 0;
		if (n == 1) {
			i = 0;
		} else {
			i = m / 10;
		}
		String s;
		int sum;
		for (; i < m; i++) {
			s = String.valueOf(i);
			sum = 0;
			for (int j = 0; j < s.length(); j++) {
				sum += Math.pow(Double.parseDouble(String.valueOf(s.charAt(j))), n);
			}
			s = null;
			if (sum == i) {
				list.add(i);
			}
		}
		return list;
	}

	public static List<Integer> getNarcissisticNumbers2(int n) {
		List<Integer> list = new ArrayList<Integer>();
		int m = (int) Math.pow(10, n);
		int i = 0;
		if (n == 1) {
			i = 0;
		} else {
			i = m / 10;
		}
		int sum = 0;
		for (; i < m; i++) {
			if (n == 1) {
				sum = i;
			} else {
				int temp = i;
				for (int k = 1; k <= n; k++) {
					int pow = (int) Math.pow(10, n - k);
					int j = temp / (int) pow;
					temp -= pow * j;
					sum += Math.pow(j, n);
				}
			}
			if (sum == i) {
				list.add(i);
			}
			sum = 0;
		}
		return list;
	}

	public static int myAdd(int a, int b) {
		int sum;
		int carry;
		do {
			sum = a ^ b;
			carry = (a & b) << 1;//进位需要左移一位
			a = sum;
			b = carry;
		} while (carry != 0);
		return sum;
	}

	public static int mySub(int a, int b) {
		return myAdd(a, myAdd(~b, 1));
	}

	public static long myMuti(int a, int b) {
		boolean flag = (b < 0);
		if (flag)
			b = -b;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < 32; i++) {
			map.put(1 << i, i);
		}
		int sum = 0;
		while (b > 0) {
			int last = b & (~b + 1); //取得最后一个1
			int count = map.get(last);//取得相关的移位
			sum += a << count;
			b = b & (b - 1);
		}
		if (flag)
			sum = -sum;
		return sum;
	}

	public static int myDiv(int a, int b) {
		boolean flag = (a < 0) ^ (b < 0);
		if (a < 0)
			a = -a;
		if (b < 0)
			b = -b;
		if (a < b)
			return 0;
		int msb = 0;
		while ((b << msb) < a) {
			msb++;
		}
		int q = 0;
		for (int i = msb; i >= 0; i--) {
			if ((b << i) > a)
				continue;
			q |= (1 << i);
			a -= (b << i);
		}
		if (flag)
			return -q;
		return q;
	}

	public static char firstUniqChar(String str) {
		// Write your code here
		Map<Character, Integer> map = new LinkedHashMap<Character, Integer>();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (map.containsKey(c)) {
				if (map.get(c) > 1) {

				} else {
					map.put(c, map.get(c) + 1);
				}
			} else {
				map.put(c, 1);
			}
		}
		Iterator<Character> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			Character next = iterator.next();
			if (map.get(next) == 1) {
				return next.charValue();
			}
		}
		return 0;
	}

	/**
	 * 
	 * 给定一个字符串，逐个翻转字符串中的每个单词。
	
		样例
		给出s = "the sky is blue"，返回"blue is sky the"
	 * @param str
	 * @return
	 */
	public static String reverseWords(String str) {
		String[] ss = str.trim().split(" ");
		StringBuffer sb = new StringBuffer();
		for (int i = ss.length - 1; i >= 0; i--) {
			if (ss[i] != null) {
				ss[i] = ss[i].trim();
			}
			if (!ss[i].equals("") && !ss[i].equals(" ")) {
				sb.append(ss[i]);
				if (i > 0) {
					sb.append(" ");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 
		 给定一篇由大写字母、小写字母、逗号、句号组成的文章，求使文章不合法的字母数。
		文章不合法有2种情况：
		1.句子的第一个字母用了小写。
		2.不是单词的首字母用了大写。
	 * @param str
	 * @return
	 */
	public static boolean isLegalIdentifier(String str) {
		char[] charArray = str.toCharArray();
		for (char c : charArray) {
			if (('A' <= c && c <= 'Z') || ('a' <= c && c < 'z') || ('1' <= c && c <= '9') || c == '0') {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param s
	 * @return
	 */

	public int[] ss() {
		Set<Integer> set = new HashSet<Integer>();
		return new int[] { 1, 2 };
	}
}