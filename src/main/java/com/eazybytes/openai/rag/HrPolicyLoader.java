package com.eazybytes.openai.rag;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class HrPolicyLoader {
	
	@Value("classpath:Eazybytes_HR_Policies.pdf")
	Resource policyFile;

	private final VectorStore vectorStore;

	public HrPolicyLoader(VectorStore vectorStore) {
		this.vectorStore = vectorStore;
	}
	
	@PostConstruct
	public void loadPdfIntoVectorStore() {
		TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(policyFile);
		List<Document> docs = tikaDocumentReader.get();
		TextSplitter tokenTextSplitter = TokenTextSplitter.builder().withChunkSize(200).withMaxNumChunks(400).build();
		vectorStore.add(tokenTextSplitter.split(docs));
	}
}
