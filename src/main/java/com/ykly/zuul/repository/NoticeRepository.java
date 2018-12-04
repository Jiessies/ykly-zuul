package com.ykly.zuul.repository;

import com.ykly.zuul.entity.Notice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface NoticeRepository extends ElasticsearchRepository<Notice, Long> {
}
