package com.epicknife.server.util;

import java.util.Collection;
import java.util.Set;

/**
 * @author Samuel "MrOverkill" Meyers
 * License : BSD
 * Date of Creation : 01 / 23 / 2015
 */
public interface IRegistry<T>
{
	
	public T get(String key);
	
	public void put(String key, T value);
	
	public Set<String> keySet();
	
	public Collection<T> values();
	
	public boolean contains(String key);

}
