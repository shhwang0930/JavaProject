package com.mysite.sbb;

import com.mysite.sbb.entity.Answer;
import com.mysite.sbb.entity.Question;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mysite.sbb.repository.AnswerRepository;
import com.mysite.sbb.repository.QuestionRepository;
import com.mysite.sbb.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionService questionService;

	@Test
	void testFind__List(){
		List<Question> all = this.questionRepository.findAll();
		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("what is sbb?", q.getSubject());
	}

	@Test
	void testFind__id(){
		Optional<Question> oq = this.questionRepository.findById(1);
		if(oq.isPresent()){
			Question q = oq.get();
			assertEquals("what is sbb?", q.getSubject());
		}
	}


	@Test
	void testFind__Sub(){
		Question q = this.questionRepository.findBySubject("what is sbb?");
		assertEquals(1, q.getId());
	}

	@Test
	void testFind__특정문자열(){
		List<Question> qList = this.questionRepository.findBySubjectLike("%sbb?");
		Question q = qList.get(0);
		assertEquals("what is sbb?", q.getSubject());
	}

	@Test
	void 데이터__수정(){
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");
		this.questionRepository.save(q);
	}

	@Test
	void 데이터__삭제(){
		assertEquals(2, this.questionRepository.count());
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);
		assertEquals(1, this.questionRepository.count());
	}

	@Test
	void 데이터__생성_후__저장(){
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q); // 어떤 질문의 답변인지 알기위해 Question객체 필요
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);
	}

	@Test
	void 답변__조회(){
		Optional<Answer> oa = this.answerRepository.findById(1);
		assertTrue(oa.isPresent());
		Answer a = oa.get();
		assertEquals(2, a.getQuestion().getId());
	}

	@Test
	void testJpa() {
		for (int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			this.questionService.create(subject, content);
		}
	}
}
