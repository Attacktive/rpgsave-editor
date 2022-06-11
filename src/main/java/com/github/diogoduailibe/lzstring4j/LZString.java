/*
 * Taken from https://github.com/diogoduailibe/lzstring4j/blob/master/src/main/java/com/github/diogoduailibe/lzstring4j/LZString.java,
 * and modified.
 */
package com.github.diogoduailibe.lzstring4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
public class LZString {
	static String keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

	public static String compress(String uncompressed) {
		if (uncompressed == null) { return ""; }

		int value;
		HashMap<String, Integer> context_dictionary = new HashMap<>();
		HashSet<String> context_dictionaryToCreate = new HashSet<>();
		String context_c;
		String context_wc;
		String context_w = "";

		// Compensate for the first entry which should not count
		double context_enlargeIn = 2d;
		int context_dictSize = 3;
		int context_numBits = 2;
		String context_data_string;
		int context_data_val = 0;
		int context_data_position = 0;

		StringBuilder context_data_stringBuilder1 = new StringBuilder();
		for (int ii = 0; ii < uncompressed.length(); ii += 1) {
			context_c = "" + (uncompressed.charAt(ii));
			if (!context_dictionary.containsKey(context_c)) {
				context_dictionary.put(context_c, context_dictSize++);
				context_dictionaryToCreate.add(context_c);
			}

			context_wc = context_w + context_c;

			if (context_dictionary.containsKey(context_wc)) {
				context_w = context_wc;
			} else {
				if (context_dictionaryToCreate.contains(context_w)) {
					if (((int)context_w.charAt(0)) < 256) {
						for (int i = 0; i < context_numBits; i++) {
							context_data_val = (context_data_val << 1);
							if (context_data_position == 15) {
								context_data_position = 0;
								context_data_stringBuilder1.append((char)context_data_val);
								context_data_val = 0;
							} else {
								context_data_position++;
							}
						}
						value = context_w.charAt(0);
						for (int i = 0; i < 8; i++) {
							context_data_val = (context_data_val << 1) | (value & 1);
							if (context_data_position == 15) {
								context_data_position = 0;
								context_data_stringBuilder1.append((char)context_data_val);
								context_data_val = 0;
							} else {
								context_data_position++;
							}
							value = value >> 1;
						}
					} else {
						value = 1;
						for (int i = 0; i < context_numBits; i++) {
							context_data_val = (context_data_val << 1) | value;
							if (context_data_position == 15) {
								context_data_position = 0;
								context_data_stringBuilder1.append((char)context_data_val);
								context_data_val = 0;
							} else {
								context_data_position++;
							}
							value = 0;
						}
						value = context_w.charAt(0);
						for (int i = 0; i < 16; i++) {
							context_data_val = (context_data_val << 1) | (value & 1);
							if (context_data_position == 15) {
								context_data_position = 0;
								context_data_stringBuilder1.append((char)context_data_val);
								context_data_val = 0;
							} else {
								context_data_position++;
							}
							value = value >> 1;
						}
					}
					context_enlargeIn--;
					if (Double.valueOf(context_enlargeIn).intValue() == 0) {
						context_enlargeIn = Math.pow(2, context_numBits);
						context_numBits++;
					}
					context_dictionaryToCreate.remove(context_w);
				} else {
					value = context_dictionary.get(context_w);
					for (int i = 0; i < context_numBits; i++) {
						context_data_val = (context_data_val << 1) | (value & 1);
						if (context_data_position == 15) {
							context_data_position = 0;
							context_data_stringBuilder1.append((char)context_data_val);
							context_data_val = 0;
						} else {
							context_data_position++;
						}
						value = value >> 1;
					}
				}
				context_enlargeIn--;
				if (Double.valueOf(context_enlargeIn).intValue() == 0) {
					context_enlargeIn = Math.pow(2, context_numBits);
					context_numBits++;
				}
				// Add wc to the dictionary.
				context_dictionary.put(context_wc, context_dictSize++);
				context_w = context_c;
			}
		}
		context_data_string = context_data_stringBuilder1.toString();

		// Output the code for w.
		if (!"".equals(context_w)) {
			if (context_dictionaryToCreate.contains(context_w)) {
				if (((int)context_w.charAt(0)) < 256) {
					StringBuilder context_data_stringBuilder = new StringBuilder(context_data_string);
					for (int i = 0; i < context_numBits; i++) {
						context_data_val = (context_data_val << 1);
						if (context_data_position == 15) {
							context_data_position = 0;
							context_data_stringBuilder.append((char)context_data_val);
							context_data_val = 0;
						} else {
							context_data_position++;
						}
					}
					context_data_string = context_data_stringBuilder.toString();
					value = context_w.charAt(0);
					StringBuilder context_data_stringBuilder2 = new StringBuilder(context_data_string);
					for (int i = 0; i < 8; i++) {
						context_data_val = (context_data_val << 1)
							| (value & 1);
						if (context_data_position == 15) {
							context_data_position = 0;
							context_data_stringBuilder2.append((char)context_data_val);
							context_data_val = 0;
						} else {
							context_data_position++;
						}
						value = value >> 1;
					}
					context_data_string = context_data_stringBuilder2.toString();
				} else {
					value = 1;
					StringBuilder context_data_stringBuilder = new StringBuilder(context_data_string);
					for (int i = 0; i < context_numBits; i++) {
						context_data_val = (context_data_val << 1) | value;
						if (context_data_position == 15) {
							context_data_position = 0;
							context_data_stringBuilder.append((char)context_data_val);
							context_data_val = 0;
						} else {
							context_data_position++;
						}
						value = 0;
					}
					context_data_string = context_data_stringBuilder.toString();
					value = context_w.charAt(0);
					StringBuilder context_data_stringBuilder2 = new StringBuilder(context_data_string);
					for (int i = 0; i < 16; i++) {
						context_data_val = (context_data_val << 1)
							| (value & 1);
						if (context_data_position == 15) {
							context_data_position = 0;
							context_data_stringBuilder2.append((char)context_data_val);
							context_data_val = 0;
						} else {
							context_data_position++;
						}
						value = value >> 1;
					}
					context_data_string = context_data_stringBuilder2.toString();
				}
				context_enlargeIn--;
				if (Double.valueOf(context_enlargeIn).intValue() == 0) {
					context_enlargeIn = Math.pow(2, context_numBits);
					context_numBits++;
				}
				context_dictionaryToCreate.remove(context_w);
			} else {
				value = context_dictionary.get(context_w);
				StringBuilder context_data_stringBuilder = new StringBuilder(context_data_string);
				for (int i = 0; i < context_numBits; i++) {
					context_data_val = (context_data_val << 1) | (value & 1);
					if (context_data_position == 15) {
						context_data_position = 0;
						context_data_stringBuilder.append((char)context_data_val);
						context_data_val = 0;
					} else {
						context_data_position++;
					}
					value = value >> 1;
				}
				context_data_string = context_data_stringBuilder.toString();
			}
			context_enlargeIn--;
			if (Double.valueOf(context_enlargeIn).intValue() == 0) {
				context_numBits++;
			}
		}

		StringBuilder context_data_stringBuilder = new StringBuilder(context_data_string);
		// Mark the end of the stream
		value = 2;
		for (int i = 0; i < context_numBits; i++) {
			context_data_val = (context_data_val << 1) | (value & 1);
			if (context_data_position == 15) {
				context_data_position = 0;
				context_data_stringBuilder.append((char)context_data_val);
				context_data_val = 0;
			} else {
				context_data_position++;
			}
			value = value >> 1;
		}
		context_data_string = context_data_stringBuilder.toString();

		// Flush the last char
		while (true) {
			context_data_val = (context_data_val << 1);
			if (context_data_position == 15) {
				context_data_string += (char)context_data_val;
				break;
			} else { context_data_position++; }
		}
		return context_data_string;
	}

	public static String decompressHexString(String hexString) {
		if (hexString == null) {
			return "";
		}

		if (hexString.length() % 2 != 0) {
			throw new RuntimeException("Input string length should be divisible by two");
		}

		int[] intArr = new int[hexString.length() / 2];

		for (int i = 0, k = 0; i < hexString.length(); i += 2, k++) {
			intArr[k] = Integer.parseInt("" + hexString.charAt(i) + hexString.charAt(i + 1), 16);
		}

		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < intArr.length; j += 2) {
			sb.append(Character.toChars(intArr[j] | intArr[j + 1] << 8));
		}

		return decompress(sb.toString());
	}

	public static String decompress(String compressed) {
		if (compressed == null) { return ""; }
		if (compressed.equals("")) { return null; }

		List<String> dictionary = new ArrayList<>(200);
		double enlargeIn = 4;
		int dictSize = 4;
		int numBits = 3;
		String entry;
		StringBuilder result;
		String w;
		int bits = 0;
		int resb;
		double maxPower = Math.pow(2, 2);
		int power = 1;
		String c = "";
		int d;
		Data data = Data.getInstance();
		data.string = compressed;
		data.val = compressed.charAt(0);
		data.position = 32768;
		data.index = 1;

		for (int i = 0; i < 3; i += 1) {
			dictionary.add(i, "");
		}

		while (power != Double.valueOf(maxPower).intValue()) {
			resb = data.val & data.position;
			data.position >>= 1;
			if (data.position == 0) {
				data.position = 32768;
				data.val = data.string.charAt(data.index++);
			}
			bits |= (resb > 0? 1: 0) * power;
			power <<= 1;
		}

		switch (bits) {
			case 0:
				maxPower = Math.pow(2, 8);
				power = 1;
				while (power != Double.valueOf(maxPower).intValue()) {
					resb = data.val & data.position;
					data.position >>= 1;
					if (data.position == 0) {
						data.position = 32768;
						data.val = data.string.charAt(data.index++);
					}
					bits |= (resb > 0? 1: 0) * power;
					power <<= 1;
				}
				c += (char)bits;
				break;
			case 1:
				bits = 0;
				maxPower = Math.pow(2, 16);
				power = 1;
				while (power != Double.valueOf(maxPower).intValue()) {
					resb = data.val & data.position;
					data.position >>= 1;
					if (data.position == 0) {
						data.position = 32768;
						data.val = data.string.charAt(data.index++);
					}
					bits |= (resb > 0? 1: 0) * power;
					power <<= 1;
				}
				c += (char)bits;
				break;
			case 2:
				return "";
		}

		dictionary.add(3, c);
		w = c;
		result = new StringBuilder(200);
		result.append(c);

		// w = result = c;

		while (true) {
			if (data.index > data.string.length()) {
				return "";
			}

			bits = 0;
			maxPower = Math.pow(2, numBits);
			power = 1;
			while (power != Double.valueOf(maxPower).intValue()) {
				resb = data.val & data.position;
				data.position >>= 1;
				if (data.position == 0) {
					data.position = 32768;
					data.val = data.string.charAt(data.index++);
				}
				bits |= (resb > 0? 1: 0) * power;
				power <<= 1;
			}

			switch (d = bits) {
				case 0:
					maxPower = Math.pow(2, 8);
					power = 1;
					while (power != Double.valueOf(maxPower).intValue()) {
						resb = data.val & data.position;
						data.position >>= 1;
						if (data.position == 0) {
							data.position = 32768;
							data.val = data.string.charAt(data.index++);
						}
						bits |= (resb > 0? 1: 0) * power;
						power <<= 1;
					}

					String temp = "";
					temp += (char)bits;
					dictionary.add(dictSize++, temp);

					d = dictSize - 1;

					enlargeIn--;

					break;
				case 1:
					bits = 0;
					maxPower = Math.pow(2, 16);
					power = 1;
					while (power != Double.valueOf(maxPower).intValue()) {
						resb = data.val & data.position;
						data.position >>= 1;
						if (data.position == 0) {
							data.position = 32768;
							data.val = data.string.charAt(data.index++);
						}
						bits |= (resb > 0? 1: 0) * power;
						power <<= 1;
					}

					temp = "";
					temp += (char)bits;

					dictionary.add(dictSize++, temp);

					d = dictSize - 1;

					enlargeIn--;

					break;
				case 2:
					return result.toString();
			}

			if (Double.valueOf(enlargeIn).intValue() == 0) {
				enlargeIn = Math.pow(2, numBits);
				numBits++;
			}

			if (d < dictionary.size() && dictionary.get(d) != null) {
				entry = dictionary.get(d);
			} else {
				if (d == dictSize) {
					entry = w + w.charAt(0);
				} else {
					return null;
				}
			}

			result.append(entry);

			// Add w+entry[0] to the dictionary.
			dictionary.add(dictSize++, w + entry.charAt(0));
			enlargeIn--;

			w = entry;

			if (Double.valueOf(enlargeIn).intValue() == 0) {
				enlargeIn = Math.pow(2, numBits);
				numBits++;
			}
		}
	}

	public static String compressToUTF16(String input) {
		if (input == null) { return ""; }

		String output;
		int c;
		int current = 0;
		int status = 0;

		String compressed = LZString.compress(input);

		StringBuilder outputBuilder = new StringBuilder();
		for (int i = 0; i < compressed.length(); i++) {
			c = compressed.charAt(i);
			outputBuilder.append((char)((current + (c >> (status + 1))) + 32));
			switch (status) {
				case 0 -> current = (c & 1) << 14;
				case 1 -> current = (c & 3) << 13;
				case 2 -> current = (c & 7) << 12;
				case 3 -> current = (c & 15) << 11;
				case 4 -> current = (c & 31) << 10;
				case 5 -> current = (c & 63) << 9;
				case 6 -> current = (c & 127) << 8;
				case 7 -> current = (c & 255) << 7;
				case 8 -> current = (c & 511) << 6;
				case 9 -> current = (c & 1023) << 5;
				case 10 -> current = (c & 2047) << 4;
				case 11 -> current = (c & 4095) << 3;
				case 12 -> current = (c & 8191) << 2;
				case 13 -> current = (c & 16383) << 1;
				case 14 -> {
					outputBuilder.append((char)((c & 32767) + 32));
					status = 0;
				}
			}

			status++;
		}
		output = outputBuilder.toString();

		output += (char)(current + 32);

		return output;
	}

	public static String decompressFromUTF16(String input) {
		if (input == null) { return ""; }
		StringBuilder output = new StringBuilder(200);
		int current = 0, c, status = 0, i = 0;

		while (i < input.length()) {
			c = (((int)input.charAt(i)) - 32);

			switch (status++) {
				case 0 -> current = c << 1;
				case 1 -> {
					output.append((char)(current | (c >> 14)));
					current = (c & 16383) << 2;
				}
				case 2 -> {
					output.append((char)(current | (c >> 13)));
					current = (c & 8191) << 3;
				}
				case 3 -> {
					output.append((char)(current | (c >> 12)));
					current = (c & 4095) << 4;
				}
				case 4 -> {
					output.append((char)(current | (c >> 11)));
					current = (c & 2047) << 5;
				}
				case 5 -> {
					output.append((char)(current | (c >> 10)));
					current = (c & 1023) << 6;
				}
				case 6 -> {
					output.append((char)(current | (c >> 9)));
					current = (c & 511) << 7;
				}
				case 7 -> {
					output.append((char)(current | (c >> 8)));
					current = (c & 255) << 8;
				}
				case 8 -> {
					output.append((char)(current | (c >> 7)));
					current = (c & 127) << 9;
				}
				case 9 -> {
					output.append((char)(current | (c >> 6)));
					current = (c & 63) << 10;
				}
				case 10 -> {
					output.append((char)(current | (c >> 5)));
					current = (c & 31) << 11;
				}
				case 11 -> {
					output.append((char)(current | (c >> 4)));
					current = (c & 15) << 12;
				}
				case 12 -> {
					output.append((char)(current | (c >> 3)));
					current = (c & 7) << 13;
				}
				case 13 -> {
					output.append((char)(current | (c >> 2)));
					current = (c & 3) << 14;
				}
				case 14 -> {
					output.append((char)(current | (c >> 1)));
					current = (c & 1) << 15;
				}
				case 15 -> {
					output.append((char)(current | c));
					status = 0;
				}
			}

			i++;
		}

		return LZString.decompress(output.toString());
		// return output;

	}

	public static String decompressFromBase64(String input) {
		return LZString.decompress(decode64(input));
	}

	// implemented from JS version
	public static String decode64(String input) {
		StringBuilder str = new StringBuilder(200);

		int ol = 0;
		int output_ = 0;
		int chr1, chr2, chr3;
		int enc1, enc2, enc3, enc4;
		int i = 0;

		while (i < input.length()) {
			enc1 = keyStr.indexOf(input.charAt(i++));
			enc2 = keyStr.indexOf(input.charAt(i++));
			enc3 = keyStr.indexOf(input.charAt(i++));
			enc4 = keyStr.indexOf(input.charAt(i++));

			chr1 = (enc1 << 2) | (enc2 >> 4);
			chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
			chr3 = ((enc3 & 3) << 6) | enc4;

			if (ol % 2 == 0) {
				output_ = chr1 << 8;

				if (enc3 != 64) {
					str.append((char)(output_ | chr2));
				}
				if (enc4 != 64) {
					output_ = chr3 << 8;
				}
			} else {
				str.append((char)(output_ | chr1));

				if (enc3 != 64) {
					output_ = chr2 << 8;
				}
				if (enc4 != 64) {
					str.append((char)(output_ | chr3));
				}
			}
			ol += 3;
		}

		return str.toString();
	}

	public static String encode64(String input) {
		StringBuilder result = new StringBuilder((input.length() * 8 + 1) / 3);
		for (int i = 0, max = input.length() << 1; i < max; ) {
			int left = max - i;
			if (left >= 3) {
				int ch1 = (input.charAt(i >> 1) >> ((1 - (i & 1)) << 3)) & 0xff;
				i++;
				int ch2 = (input.charAt(i >> 1) >> ((1 - (i & 1)) << 3)) & 0xff;
				i++;
				int ch3 = (input.charAt(i >> 1) >> ((1 - (i & 1)) << 3)) & 0xff;
				i++;
				result.append(keyStr.charAt((ch1 >> 2) & 0x3f));
				result.append(keyStr.charAt(((ch1 << 4) + (ch2 >> 4)) & 0x3f));
				result.append(keyStr.charAt(((ch2 << 2) + (ch3 >> 6)) & 0x3f));
				result.append(keyStr.charAt(ch3 & 0x3f));
			} else if (left == 2) {
				int ch1 = (input.charAt(i >> 1) >> ((1 - (i & 1)) << 3)) & 0xff;
				i++;
				int ch2 = (input.charAt(i >> 1) >> ((1 - (i & 1)) << 3)) & 0xff;
				i++;
				result.append(keyStr.charAt((ch1 >> 2) & 0x3f));
				result.append(keyStr.charAt(((ch1 << 4) + (ch2 >> 4)) & 0x3f));
				result.append(keyStr.charAt(((ch2 << 2)) & 0x3f));
				result.append('=');
			} else if (left == 1) {
				int ch1 = (input.charAt(i >> 1) >> ((1 - (i & 1)) << 3)) & 0xff;
				i++;
				result.append(keyStr.charAt((ch1 >> 2) & 0x3f));
				result.append(keyStr.charAt(((ch1 << 4)) & 0x3f));
				result.append('=');
				result.append('=');
			}
		}
		return result.toString();
	}

	public static String compressToBase64(String input) {
		return encode64(compress(input));
	}
}

class Data {
	public int val;
	public String string;
	public int position;
	public int index;

	public static Data getInstance() {
		return new Data();
	}
}
