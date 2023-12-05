package com.vocabulary;

import java.util.List;

public interface LazyLoad {
	
	public List<Word> lazyRead();
	
	public void lazyWrite(List<Word> list);
	
	public List<Word> lazyReadAndWrite(List<Word> list);
	
	
	
}