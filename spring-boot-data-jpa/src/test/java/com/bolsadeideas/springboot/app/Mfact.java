package com.bolsadeideas.springboot.app;

public class Mfact {
	public static void main(String[] args) {
		
		System.out.println(Mfact.factorial(5));
	} 
	
	
	public static int factorial(int num ) {
		System.out.println("num : "+num); 
		if (num<=1) {
			return 1; 
		}else {
			return num * factorial(num-1); 
		}
 
	}
	

}
