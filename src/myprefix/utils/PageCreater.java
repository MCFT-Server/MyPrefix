package myprefix.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageCreater {
	private int pageCount;

	public PageCreater() {
		this(5);
	}

	public PageCreater(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int count) {
		pageCount = count;
	}

	public <T> T[] getPage(T[] args, int page) {
		return Arrays.copyOfRange(args, page * getPageCount() - getPageCount(), page * getPageCount());
	}
	
	public <T> List<T> getPage(List<T> args, int page) {
		if (page < 1) {
			return new ArrayList<>();
		}
		int fromindex = page * getPageCount() - getPageCount();
		int toindex = page * getPageCount();
		if (args.size() - 1 >= fromindex && args.size() - 1 <= toindex)
			toindex = args.size();
		else 
			if (args.size() < toindex) {
				return new ArrayList<>();
			}
		return args.subList(fromindex, toindex);
	}
}
