package it.cb.rective.test;

import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServer;
import reactor.netty.tcp.TcpServer;

public class Person {

	public Person() { }

	public Person(String name, String surname) {
		this.name = name;
		this.surname = surname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	private String name;

	private String surname;

	@Override
	public String toString() {
		return "Person{" +
			   "name='" + name + '\'' +
			   ", surname='" + surname + '\'' +
			   '}';
	}

}
