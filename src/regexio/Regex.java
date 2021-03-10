package regexio;

import java.util.ArrayList;
import java.util.List;

/**
 * This class can be used to create regex patterns in string
 * form. The patterns can be built using a builder pattern, 
 * which makes the regex more readable and more accessible.
 * 
 * After all regex operations have been defined, the regex can
 * be built out into a string.
 * 
 * @author jonas.franz
 *
 */
public class Regex {

			/* ---< NESTED >--- */
	/**
	 * Interface to pass lambda function that builds
	 * regex pattern for corresponding enum field.
	 * 
	 * @author jonas.franz
	 *
	 */
	protected interface RegexBuilder {
		
		/**
		 * This method builds the regex string based on
		 * the operation type and the given arguments.
		 * @param arguments The arguments that are passed in
		 * @return The built regex string.
		 */
		public String build(Object [] arguments);
		
	}
	
	/**
	 * The operation type is responsible to denote the type
	 * of the regex operation. Each field corresponds to a
	 * different regex operation, and contains a lambda that
	 * builds the regex string accordingly. Also, arguments
	 * are passed in to create custom patterns based on the
	 * passed arguments.
	 * 
	 * @author jonas.franz
	 *
	 */
	protected static enum RegexOperationType {
		
				/* ---< CHARACTER >--- */
		/** Matches any character. */
		ANY_CHARACTER(arguments -> {
			return ".";
		}),
		
		/** Matches a given character. */
		CHARACTER(arguments -> {
			return "" + (char) arguments [0];
		}),
		
		/** Matches any digit */
		ANY_DIGIT(arguments -> {
			return "\\d";
		}),
		
		/** Matches any letter. */
		ANY_LETTER(arguments -> {
			return "\\w";
		}),
		
				/* ---< QUANTIFIERS >--- */
		/** Repeat once or multiple times. */
		ONE_OR_MORE(arguments -> {
			return "(" + ((Regex) arguments [0]).build() + ")+";
		}),
		
		/** Repeat exactly n times */
		REPEAT_N(arguments -> {
			return "(" + ((Regex) arguments [0]).build() + "){" + (int) arguments [1] + "}";
		}),
		
		/** Repeat between n and m times. */
		REPEAT_N_TO_M(arguments -> {
			return "(" + ((Regex) arguments [0]).build() + "){" + (int) arguments [1] + "," + (int) arguments [2] + "}";
		}),
		
		/** Repeat at least once */
		REPEAT_N_OR_MORE(arguments -> {
			return "(" + ((Regex) arguments [0]).build() + "){" + (int) arguments [1] + ",}";
		}),
		
		/** Repeat multiple times or never */
		MULTIPLE_OR_NONE(arguments -> {
			return "(" + ((Regex) arguments [0]).build() + ")*";
		}),
		
		/** Repeat once or never */
		ONCE_OR_NONE(arguments -> {
			return "(" + ((Regex) arguments [0]).build() + ")?";
		}),
		
				/* ---< LOGIC >--- */
		/** Match one of multiple regexes */
		OR(arguments -> {
			String pattern = "(";
			
			for (Object obj : arguments) 
				pattern += ((Regex) obj).build() + "|";
			
			pattern = pattern.substring(0, pattern.length() - 1);
			
			pattern += ")";
			
			return pattern;
		}),
		
				/* ---< WHITESPACES >--- */
		/** Match whitespace character */
		WHITESPACE(arguments -> {
			return "\\s";
		}),
		
		/** Match tab character */
		TABULATOR(arguments -> {
			return "\\t";
		});
		
		/**
		 * Builder that encapsulates the various regex string builders.
		 * For each enum field, a lambda is passed through the builder, 
		 * and is called when the operation is built.
		 */
		RegexBuilder builder;
		
		
				/* ---< CONSTRUCTORS >--- */
		/**
		 * Default constructor, sets builder.
		 * @param builder The builder for this operation type.
		 */
		RegexOperationType(RegexBuilder builder) {
			this.builder = builder;
		}
		
	}
	
	/**
	 * Represents a single regex element, or a single
	 * matching element. Multiple operations can be chained
	 * together to form a more complex pattern.
	 * 
	 * @author jonas.franz
	 *
	 */
	protected class RegexOperation {
		
				/* ---< FIELDS >--- */
		/**
		 * The type of this operation.
		 */
		protected RegexOperationType type;
		
		/**
		 * The arguments for this operation. Arguments are passed
		 * into the builder for this operation for customizable values
		 * inside the regex.
		 */
		protected Object [] arguments;
		
		
				/* ---< CONSTRUCTORS >--- */
		/**
		 * Constructor without custom arguments.
		 * @param type The type of this operation.
		 */
		protected RegexOperation(RegexOperationType type) {
			this.type = type;
		}
		
		/**
		 * Constructor with custom arguments.
		 * @param type The type of this operation.
		 * @param arguments The custom arguments of this operation.
		 */
		protected RegexOperation(RegexOperationType type, Object...arguments) {
			this.type = type;
			this.arguments = arguments;
		}
		
		/**
		 * Builds this regex operation by using the build() method
		 * of the builder that is capsuled in the operation type,
		 * and using the arguments stored in the operation.
		 * @return Returns a string that represents this regex operation.
		 */
		protected String build() {
			return this.type.builder.build(this.arguments);
		}
		
	}
	
	
			/* ---< FIELDS >--- */
	/**
	 * All operations in this regex. When the regex is built, all 
	 * operations are concatenated after one another to form the
	 * final regex.
	 */
	private List<RegexOperation> operations = new ArrayList();
	
	
			/* ---< CONSTRUCTORS >--- */
	/**
	 * Default constructor. All regex operations are created via
	 * the builder pattern and builder functions.
	 */
	public Regex() {
		
	}
	
	
			/* ---< METHODS >--- */
	/**
	 * Builds the Regex as a String. The resulting string represents
	 * the this regex and can be used with the <code>("abc").matches(pattern)</code>
	 * method.
	 * @return A string that represents this regex.
	 */
	public String build() {
		String regex = "";
		
		/* Concat all operations */
		for (RegexOperation operation : this.operations) 
			regex += operation.build();
		
		return regex;
	}
	
	
			/* ---< CHARACTERS >--- */
	/**
	 * Matches any character.
	 * @return Returns self for builder.
	 */
	public Regex anyCharacter() {
		this.operations.add(new RegexOperation(RegexOperationType.ANY_CHARACTER));
		return this;
	}
	
	/**
	 * Matches any digit: 0, 1, 2, ...
	 * @return Returns self for builder.
	 */
	public Regex anyDigit() {
		this.operations.add(new RegexOperation(RegexOperationType.ANY_DIGIT));
		return this;
	}
	
	/**
	 * Matches any word letter, which includes uppercase and
	 * lowercase letters, underscores and digits.
	 * @return Returns self for builder.
	 */
	public Regex anyLetter() {
		this.operations.add(new RegexOperation(RegexOperationType.ANY_LETTER));
		return this;
	}
	
	/**
	 * Matches any word letter, which includes uppercase and
	 * lowercase letters, underscores and digits.
	 * @param c The character to match.
	 * @return Returns self for builder.
	 */
	public Regex character(char c) {
		this.operations.add(new RegexOperation(RegexOperationType.CHARACTER, c));
		return this;
	}
	
	
			/* ---< QUANTIFIERS >--- */
	/**
	 * Matches the given regex once once or multiple times.
	 * @param regex The regex to match.
	 * @return Returns self for builder.
	 */
	public Regex onceOrMore(Regex regex) {
		this.operations.add(new RegexOperation(RegexOperationType.ONE_OR_MORE, regex));
		return this;
	}
	
	/**
	 * Matches the given regex the given amount of times.
	 * @param regex The regex to match.
	 * @param amount The amount of matches
	 * @return Returns self for builder.
	 */
	public Regex repeat(Regex regex, int amount) {
		assert amount > 0 : "Expected a number greater than 0!";
		this.operations.add(new RegexOperation(RegexOperationType.REPEAT_N, regex, amount));
		return this;
	}
	
	/**
	 * Matches the given regex in the given range amount of times.
	 * @param regex The regex to match.
	 * @param lower The minimum amount of repeated matches.
	 * @param upper The maximum amount of repeated matches.
	 * @return Returns self for builder.
	 */
	public Regex repeat(Regex regex, int lower, int upper) {
		assert lower <= upper : "Expected second parameter to be less or equal to third parameter!";
		this.operations.add(new RegexOperation(RegexOperationType.REPEAT_N_TO_M, regex, lower, upper));
		return this;
	}
	
	/**
	 * Matches the given regex at least the given amount of times.
	 * @param regex The regex to match.
	 * @param amount The minimum number of matches.
	 * @return Returns self for builder.
	 */
	public Regex nOrMore(Regex regex, int amount) {
		assert amount > 0 : "Expected a number greater than 0!";
		this.operations.add(new RegexOperation(RegexOperationType.REPEAT_N_OR_MORE, regex, amount));
		return this;
	}
	
	/**
	 * Matches the given regex multiple times or never.
	 * @param regex The regex to match.
	 * @return Returns self for builder.
	 */
	public Regex multipleOrNone(Regex regex) {
		this.operations.add(new RegexOperation(RegexOperationType.MULTIPLE_OR_NONE, regex));
		return this;
	}
	
	/**
	 * Matches the given regex once or never.
	 * @param regex The regex to match.
	 * @return Returns self for builder.
	 */
	public Regex onceOrNone(Regex regex) {
		this.operations.add(new RegexOperation(RegexOperationType.ONCE_OR_NONE, regex));
		return this;
	}
	
			/* ---< LOGIC >--- */
	/**
	 * Matches one of the given regexes.
	 * @param regexes All regexes to choose one from.
	 * @return Returns self for builder.
	 */
	public Regex or(Regex...regexes) {
		assert regexes.length > 1 : "Expected at least two regexes!";
		this.operations.add(new RegexOperation(RegexOperationType.OR, (Object []) regexes));
		return this;
	}
	
			/* ---< WHITESPACES >--- */
	/**
	 * Matches a whitespace character.
	 * @return Returns self for builder.
	 */
	public Regex whitespace() {
		this.operations.add(new RegexOperation(RegexOperationType.WHITESPACE));
		return this;
	}
	
	/**
	 * Matches a tabulator character.
	 * @return Returns self for builder.
	 */
	public Regex tabulator() {
		this.operations.add(new RegexOperation(RegexOperationType.TABULATOR));
		return this;
	}
	
}
