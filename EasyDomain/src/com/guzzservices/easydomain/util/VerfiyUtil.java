package com.guzzservices.easydomain.util;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class VerfiyUtil {
		
	public static String getWanCode(int randCode) throws Exception{
		String url = "http://diy.hichina.com/cgi-bin/sImage?Rand=" + randCode ;
		
		BufferedImage bi = ImageIO.read(new URL(url)) ;
		
		int width = bi.getWidth() ;
		int height = bi.getHeight() ;
				
		for(int i = 0 ; i < width ; i++){
			for(int j = 0 ; j < height ; j++){
				int rpg = bi.getRGB(i, j) ;
				
				if(rpg == -4144960){
					bi.setRGB(i, j, -1) ;
				}
			}
		}
		
		StringBuffer code = new StringBuffer() ;
		
		boolean wordPixelStart = false ;		
		int numOfPixelInThisWord = 0 ;
		int numOfPixelInThisWordTopHalf = 0 ;
		
		for(int x = 0 ; x < width ; x++){
			
			int wordPixNum = 0 ;
			
			for(int y = 0 ; y < height ; y++){
				int rpg = bi.getRGB(x, y) ;
				if(rpg == -16777216){
					wordPixNum++ ;
					
					if(y < height /2){
						numOfPixelInThisWordTopHalf++ ;
					}
				}
			}
			
			if(wordPixNum > 0){
				if(!wordPixelStart){
					wordPixelStart = true ;
				}
				
				numOfPixelInThisWord+=wordPixNum ;
			}else{
				if(wordPixelStart){ //end of a wordPixel.
					String charCode = getCodeByPiexl(numOfPixelInThisWord, numOfPixelInThisWordTopHalf) ;
					
					if(charCode == null){ //error found
						return null ;
					}else{
						code.append(charCode) ;
					}

					wordPixelStart = false ;
					numOfPixelInThisWord = 0 ;
					numOfPixelInThisWordTopHalf = 0 ;
				}
				
			}
		}
		
		return code.toString() ;
	}

    private static HashMap<Integer, String> wanCodePixelMap = new HashMap<Integer, String>() ;
    static{
   		wanCodePixelMap.put(36, "0") ;
		wanCodePixelMap.put(27, "1") ;
		wanCodePixelMap.put(32, "2") ;
		wanCodePixelMap.put(31, "3") ;
		wanCodePixelMap.put(35, "4") ;
		wanCodePixelMap.put(37, "5") ;
		wanCodePixelMap.put(39, "6") ;
		wanCodePixelMap.put(26, "7") ;
		wanCodePixelMap.put(40, "8") ;
//		map.put(39, "9") ; //9和6重复了
    }
	
	protected static String getCodeByPiexl(int numOfPiexl, int numOfTopHalfPiexl){
		String value = wanCodePixelMap.get(numOfPiexl) ;
		
		if(numOfPiexl == 39 && numOfTopHalfPiexl*2 > numOfPiexl){
			return "9" ;
		}
		
		return value ;
	}
	
}
