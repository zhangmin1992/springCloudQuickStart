import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("dev")
public class TestProfile {

	@Test
	public void testProfile() {
		System.out.println("-----我执行啥看端口号是哪个");
		
	}
}
