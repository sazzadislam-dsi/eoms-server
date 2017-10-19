package com.lynas.service;

import com.lynas.model.Exam;
import com.lynas.model.response.ExamClassResponse1;
import com.lynas.model.util.ExamQueryResult;
import com.lynas.model.util.ExamType;
import com.lynas.repo.ExamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by seal on 5/28/2017.
 */
@Service
public class ExamServiceJava {

    private static final String GRAND_TOTAL = "GRAND_TOTAL";

    private ExamRepository examRepository;

    public ExamServiceJava(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Transactional
    public List<ExamClassResponse1> getResultOfClass(long classId, int year, long orgId) {
        List<ExamQueryResult> list = examRepository.resultOfClassByYear(classId, year, orgId);

        if (list == null) return new ArrayList<>();

        Map<String, Double> subjectTotalMark = calculateTotal(list);
        double grandTotal = subjectTotalMark.get(GRAND_TOTAL);
        List<ExamClassResponse1> examClassResponse1s = list
                .stream()
                .sorted(Comparator.comparingInt(ExamQueryResult::getRoleNumber))
                .collect(Collectors.groupingBy(ExamQueryResult::getRoleNumber))
                .entrySet()
                .stream()
                .map(i -> {
                    Map<String, Double> map = new HashMap<>();
                    i.getValue()
                            .forEach(j -> {
                                double total = j.getExam()
                                        .stream()
                                        .mapToDouble(k -> (k.getObtainedNumber() * k.getPercentile()) / 100)
                                        .sum();
                                map.put(j.getSubject(), total);
                            });

                    List<ExamClassResponse1.Subjects> subjectsList = i.getValue()
                            .stream()
                            .map(j -> {
                                ExamClassResponse1.Subjects subjects = new ExamClassResponse1.Subjects();
                                subjects.setSubjectName(j.getSubject());
                                Map<ExamType, Exam> examMap = j.getExam()
                                        .stream()
                                        .collect(Collectors.toMap(Exam::getExamType, Function.identity()));
                                subjects.setExams(examMap);
                                double obtainMark = map.get(j.getSubject());
                                subjects.setObtained(obtainMark);
                                subjects.setTotal(subjectTotalMark.get(j.getSubject()));
                                return subjects;
                            })
                            .collect(Collectors.toList());

                    double obtainTotal = map
                            .values()
                            .stream()
                            .mapToDouble(Double::valueOf)
                            .sum();

                    ExamClassResponse1.GrandTotal grandTotalObj = new ExamClassResponse1.GrandTotal(grandTotal, obtainTotal);
                    return new ExamClassResponse1(i.getValue().get(0).getStudentId(),
                            classId,
                            i.getKey(),
                            year,
                            grandTotalObj,
                            subjectsList);
                })
                .collect(Collectors.toList());
        return examClassResponse1s;
    }

    private Map<String, Double> calculateTotal(List<ExamQueryResult> list) {
        Map<String, Double> map = list
                .stream()
                .collect(Collectors.toMap(ExamQueryResult::getSubject, p -> p, (p, q) -> p))
                .values()
                .stream()
                .collect(Collectors.toMap(ExamQueryResult::getSubject, i -> i.getExam()
                        .stream()
                        .mapToDouble(j -> (j.getTotalNumber() * j.getPercentile()) / 100).sum()));

        map.put(GRAND_TOTAL, map.entrySet().stream().mapToDouble(Map.Entry::getValue).sum());
        return map;
    }
}
