package regexio;

public class Main {

	public static void main(String[] args) {
		
		String pattern = new Regex()
				.anyCharacter()
				.character('c')
				.whitespace()
				.or(
					new Regex()
						.nOrMore(new Regex()
							.anyDigit(),
							3
					),
					new Regex()
						.character('G')
				)
		.build();
		
		System.out.println(pattern);
		System.out.println("%c G".matches(pattern));
	}

}

