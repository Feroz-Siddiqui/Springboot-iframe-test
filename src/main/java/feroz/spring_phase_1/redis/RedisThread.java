package feroz.spring_phase_1.redis;

import java.util.concurrent.Callable;

import com.github.javafaker.Faker;

public class RedisThread implements Runnable,Callable<String> {
	Faker faker ;
	
	
	public RedisThread(Faker faker) {
		super();
		this.faker = faker;
	}


	@Override
	public void run() {
		// TODO Auto-generated method stub
		

	}


	@Override
	public String call() throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Called");
    	String key =faker.superhero().name();

		RedisUtils.getInstance().insertData(key, "feroz123");
		String retrivekey = RedisUtils.getInstance().retrieveData(key);
		System.out.println("retrieve data from redis is {}"+retrivekey);
		return retrivekey;
	}

}
