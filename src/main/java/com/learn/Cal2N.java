package com.learn;

import java.util.Scanner;
/**
 * 计算2的n此方结果，可以扩展成m的n次方
 * @author chenyunh
 * @since 2018年10月9日
 */
public class Cal2N {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int n = scanner.nextInt();
		String result = "1";
		for(int i=0;i<n;i++) {
			char[] resultChars = result.toCharArray();
			int length = resultChars.length;
			boolean gaiweiAdd1 = false;
			boolean add1 = false;
			for(int j=0;j<length;j++) {
				char c = resultChars[length-j-1];
				int valueOf = Integer.parseInt(c+"");
				if(gaiweiAdd1) {
					valueOf = valueOf*2+1;
					gaiweiAdd1 = false;
				}else {
					valueOf = valueOf*2;
				}
				int chu = valueOf/10;
				int yu = valueOf%10;
				if(chu>0) {
					gaiweiAdd1 = true;
					if(length-j-1==0) {
						add1 = true;
					}
				}
				resultChars[length-j-1] = Integer.toString(yu).charAt(0);
			}
			if(add1) {
				result = "1" + String.valueOf(resultChars);
				add1 = false;
			}else {
				result = String.valueOf(resultChars);
			}
		}
		System.out.println(result);
	}
}
