package net.comptoirs.android.common.helper;

public class StringMatcher
{
	public static boolean match(String value, String keyword)
	{
		if (value == null || keyword == null) return false;
		if (keyword.length() > value.length()) return false;

		int i = 0, j = 0;
		do
		{
			if (isKorean(value.charAt(i)) && isInitialSound(keyword.charAt(j)))
			{
			}
			else
			{
				if (keyword.charAt(j) == value.charAt(i))
				{
					i++;
					j++;
				}
				else if (j > 0)
					break;
				else i++;
			}
		} while (i < value.length() && j < keyword.length());

		return (j == keyword.length()) ? true : false;
	}

	private static boolean isKorean(char c)
	{
		return false;
	}

	private static boolean isInitialSound(char c)
	{
		return false;
	}

}
