
package org.tartarus.snowball;
import java.lang.reflect.InvocationTargetException;
import java.io.Serializable;
import java.util.Arrays;

public class SnowballProgram implements Serializable {
    protected SnowballProgram()
    {
	current = new StringBuilder();
	init();
    }

    static final long serialVersionUID = 2016072500L;

    private void init() {
	cursor = 0;
	limit = current.length();
	limit_backward = 0;
	bra = cursor;
	ket = limit;
    }

    /**
     * Set the current string.
     */
    public void setCurrent(String value)
    {
        // Make a new StringBuilder.  If we reuse the old one, and a user of
        // the library keeps a reference to the buffer returned (for example,
        // by converting it to a String in a way which doesn't force a copy),
        // the buffer size will not decrease, and we will risk wasting a large
        // amount of memory.
        // Thanks to Wolfram Esser for spotting this problem.
        //The non-alphabetic characters are removed at the begining, AFTER processing the string
        String newvalue = value.replaceAll("\\W", "");
        current = new StringBuilder(newvalue);
	init();
    }

    /**
     * Get the current string.
     */
    public String getCurrent()
    {
                //The non-alphabetic characters are removed at the end, AFTER processing the string
		//This is a bad way of doing it.
		String string = current.toString();
		//Use RE to remove all non-words
                //replace the characters apart from [a-zA-Z0-9_]. If you want to replace the underscore too, then use: [\\W_]
		String result = string.replaceAll("\\W", "");
		//Give the result back
		return result;
    }

    // current string
    protected StringBuilder current;

    protected int cursor;
    protected int limit;
    protected int limit_backward;
    protected int bra;
    protected int ket;

    public SnowballProgram(SnowballProgram other) {
	current          = other.current;
	cursor           = other.cursor;
	limit            = other.limit;
	limit_backward   = other.limit_backward;
	bra              = other.bra;
	ket              = other.ket;
    }

    protected void copy_from(SnowballProgram other)
    {
	current          = other.current;
	cursor           = other.cursor;
	limit            = other.limit;
	limit_backward   = other.limit_backward;
	bra              = other.bra;
	ket              = other.ket;
    }

    protected boolean in_grouping(char [] s, int min, int max)
    {
	if (cursor >= limit) return false;
	char ch = current.charAt(cursor);
	if (ch > max || ch < min) return false;
	ch -= min;
	if ((s[ch >> 3] & (0X1 << (ch & 0X7))) == 0) return false;
	cursor++;
	return true;
    }

    protected boolean in_grouping_b(char [] s, int min, int max)
    {
	if (cursor <= limit_backward) return false;
	char ch = current.charAt(cursor - 1);
	if (ch > max || ch < min) return false;
	ch -= min;
	if ((s[ch >> 3] & (0X1 << (ch & 0X7))) == 0) return false;
	cursor--;
	return true;
    }

    protected boolean out_grouping(char [] s, int min, int max)
    {
	if (cursor >= limit) return false;
	char ch = current.charAt(cursor);
	if (ch > max || ch < min) {
	    cursor++;
	    return true;
	}
	ch -= min;
	if ((s[ch >> 3] & (0X1 << (ch & 0X7))) == 0) {
	    cursor++;
	    return true;
	}
	return false;
    }

    protected boolean out_grouping_b(char [] s, int min, int max)
    {
	if (cursor <= limit_backward) return false;
	char ch = current.charAt(cursor - 1);
	if (ch > max || ch < min) {
	    cursor--;
	    return true;
	}
	ch -= min;
	if ((s[ch >> 3] & (0X1 << (ch & 0X7))) == 0) {
	    cursor--;
	    return true;
	}
	return false;
    }

    protected boolean eq_s(CharSequence s)
    {
	if (limit - cursor < s.length()) return false;
	int i;
	for (i = 0; i != s.length(); i++) {
	    if (current.charAt(cursor + i) != s.charAt(i)) return false;
	}
	cursor += s.length();
	return true;
    }

    protected boolean eq_s_b(CharSequence s)
    {
	if (cursor - limit_backward < s.length()) return false;
	int i;
	for (i = 0; i != s.length(); i++) {
	    if (current.charAt(cursor - s.length() + i) != s.charAt(i)) return false;
	}
	cursor -= s.length();
	return true;
    }

    protected int find_among(Among v[])
    {
	int i = 0;
	int j = v.length;

	int c = cursor;
	int l = limit;

	int common_i = 0;
	int common_j = 0;

	boolean first_key_inspected = false;

	while (true) {
	    int k = i + ((j - i) >> 1);
	    int diff = 0;
	    int common = common_i < common_j ? common_i : common_j; // smaller
	    Among w = v[k];
	    int i2;
	    for (i2 = common; i2 < w.s.length; i2++) {
		if (c + common == l) {
		    diff = -1;
		    break;
		}
		diff = current.charAt(c + common) - w.s[i2];
		if (diff != 0) break;
		common++;
	    }
	    if (diff < 0) {
		j = k;
		common_j = common;
	    } else {
		i = k;
		common_i = common;
	    }
	    if (j - i <= 1) {
		if (i > 0) break; // v->s has been inspected
		if (j == i) break; // only one item in v

		// - but now we need to go round once more to get
		// v->s inspected. This looks messy, but is actually
		// the optimal approach.

		if (first_key_inspected) break;
		first_key_inspected = true;
	    }
	}
	while (true) {
	    Among w = v[i];
	    if (common_i >= w.s.length) {
		cursor = c + w.s.length;
		if (w.method == null) return w.result;
		boolean res;
		try {
		    Object resobj = w.method.invoke(this);
		    res = resobj.toString().equals("true");
		} catch (InvocationTargetException e) {
		    res = false;
		    // FIXME - debug message
		} catch (IllegalAccessException e) {
		    res = false;
		    // FIXME - debug message
		}
		cursor = c + w.s.length;
		if (res) return w.result;
	    }
	    i = w.substring_i;
	    if (i < 0) return 0;
	}
    }

    // find_among_b is for backwards processing. Same comments apply
    protected int find_among_b(Among v[])
    {
      	int i = 0;
	int j = v.length;

	int c = cursor;
	int lb = limit_backward;

	int common_i = 0;
	int common_j = 0;

	boolean first_key_inspected = false;

	while (true) {
	    int k = i + ((j - i) >> 1);
	    int diff = 0;
	    int common = common_i < common_j ? common_i : common_j;
	    Among w = v[k];
	    int i2;
	    for (i2 = w.s.length - 1 - common; i2 >= 0; i2--) {
		if (c - common == lb) {
		    diff = -1;
		    break;
		}
		diff = current.charAt(c - 1 - common) - w.s[i2];
		if (diff != 0) break;
		common++;
	    }
	    if (diff < 0) {
		j = k;
		common_j = common;
	    } else {
		i = k;
		common_i = common;
	    }
	    if (j - i <= 1) {
		if (i > 0) break;
		if (j == i) break;
		if (first_key_inspected) break;
		first_key_inspected = true;
	    }
	}
	while (true) {
	    Among w = v[i];
	    if (common_i >= w.s.length) {
		cursor = c - w.s.length;
		if (w.method == null) return w.result;

		boolean res;
		try {
		    Object resobj = w.method.invoke(this);
		    res = resobj.toString().equals("true");
		} catch (InvocationTargetException e) {
		    res = false;
		    // FIXME - debug message
		} catch (IllegalAccessException e) {
		    res = false;
		    // FIXME - debug message
		}
		cursor = c - w.s.length;
		if (res) return w.result;
	    }
	    i = w.substring_i;
	    if (i < 0) return 0;
	}
    }
    
    //---   NEW
    //Finds equal strings
    protected int find_among_b2(Among v[])
    {
      	int i = 0;
        int k = 0;
	int j = v.length;
        Among w = v[i];
        boolean flag=false;
        
	while ((i<j)&&(!flag))
        {           
            while((current.charAt(k) == v[i].s[k]) && (k < v[i].s.length-1) && (k < limit-1))
            {
                //System.out.println(current.charAt(k) + "\t\t" + v[i].s[k] + "\t"+ (limit-1)+"\t"+ (v[i].s.length-1) + "\t" + k);
                k++;
            }
            //System.out.println(k);
            if((k == v[i].s.length-1) && (limit==v[i].s.length) &&(current.charAt(k) == v[i].s[k]))
            {
                flag = true;
                //System.out.println(current.charAt(k) + "\t\t" + v[i].s[k] + "\t"+ (limit-1)+"\t"+ (v[i].s.length-1) + "\t" + k);
            }
            k=0;
            i++;
        }
	if (flag)
        {
            cursor = 0;
            return v[i-1].result;
        }
        else
            return 0;
    }

    //---   NEW
    //Finds equal strings
    protected int find_among_b3(Among v[])
    {
      	int i = 0;
        int k = current.length()-1;
        int c;
	int j = v.length-1;
        Among w = v[i];
        c = v[i].s.length;
        c--;
        boolean flag=false;
        
	while ((i<j)&&(!flag))
        {           
            //System.out.println(k+"\t"+current.charAt(k)+"\t"+c+"\t"+v[i].s[c]);
            while((current.charAt(k) == v[i].s[c]) && (c > 0))//< v[i].s.length-1) && (k < limit-1))
            {
                //System.out.println(current.charAt(k) + "\t\t" + v[i].s[c] + "\t"+ (limit-1)+"\t"+ (v[i].s.length-1) + "\t" + k);
                k--;
                c--;
            }
            //System.out.println(current.length()+"\t"+current);
            //System.out.println(v[i].s.length);
            if(current.charAt(k) == v[i].s[c])//((k == v[i].s.length-1) && (limit==v[i].s.length) &&(current.charAt(k) == v[i].s[k]))
            {
                flag = true;
                //System.out.println(current.charAt(k) + "\t\t" + v[i].s[k] + "\t"+ (limit-1)+"\t"+ (v[i].s.length-1) + "\t" + k);
            }
            if(i<j)
            {
                c = v[i+1].s.length-1;
                k=current.length()-1;
                //System.out.println(k+"\t"+current.charAt(k)+"\t"+c+"\t"+v[i+1].s[c]);
                //System.out.println(j+"\tFIN\t"+i);
            }
            i++;
        }
	if (flag)
        {
            //cursor = 0;
            return v[i-1].result;
        }
        else
            return 0;
    }
    
    
    /* to replace chars between c_bra and c_ket in current by the
     * chars in s.
     */
    protected int replace_s(int c_bra, int c_ket, String s)
    {
	int adjustment = s.length() - (c_ket - c_bra);
	current.replace(c_bra, c_ket, s);
	limit += adjustment;
	if (cursor >= c_ket) cursor += adjustment;
	else if (cursor > c_bra) cursor = c_bra;
	return adjustment;
    }

    protected void slice_check()
    {
	if (bra < 0 ||
	    bra > ket ||
	    ket > limit ||
	    limit > current.length())   // this line could be removed
	{
	    System.err.println("faulty slice operation");
	// FIXME: report error somehow.
	/*
	    fprintf(stderr, "faulty slice operation:\n");
	    debug(z, -1, 0);
	    exit(1);
	    */
	}
    }

    protected void slice_from(String s)
    {
	slice_check();
	replace_s(bra, ket, s);
    }

    protected void slice_from(CharSequence s)
    {
        slice_from(s.toString());
    }

    protected void slice_del()
    {
	slice_from("");
    }

    protected void insert(int c_bra, int c_ket, String s)
    {
	int adjustment = replace_s(c_bra, c_ket, s);
	if (c_bra <= bra) bra += adjustment;
	if (c_bra <= ket) ket += adjustment;
    }

    protected void insert(int c_bra, int c_ket, CharSequence s)
    {
	insert(c_bra, c_ket, s.toString());
    }

    /* Copy the slice into the supplied StringBuilder */
    protected void slice_to(StringBuilder s)
    {
	slice_check();
	s.replace(0, s.length(), current.substring(bra, ket));
    }

    protected void assign_to(StringBuilder s)
    {
	s.replace(0, s.length(), current.substring(0, limit));
    }

/*
extern void debug(struct SN_env * z, int number, int line_count)
{   int i;
    int limit = SIZE(z->p);
    //if (number >= 0) printf("%3d (line %4d): '", number, line_count);
    if (number >= 0) printf("%3d (line %4d): [%d]'", number, line_count,limit);
    for (i = 0; i <= limit; i++)
    {   if (z->lb == i) printf("{");
        if (z->bra == i) printf("[");
        if (z->c == i) printf("|");
        if (z->ket == i) printf("]");
        if (z->l == i) printf("}");
        if (i < limit)
        {   int ch = z->p[i];
            if (ch == 0) ch = '#';
            printf("%c", ch);
        }
    }
    printf("'\n");
}
*/

};
