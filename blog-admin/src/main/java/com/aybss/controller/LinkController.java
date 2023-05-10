package com.aybss.controller;

import com.aybss.domain.ResponseResult;
import com.aybss.domain.dto.LinkDto;
import com.aybss.domain.entity.Link;
import com.aybss.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/link")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult getAllLinks(Integer pageNum, Integer pageSize, LinkDto linkDto){
        return linkService.getAllLinks(pageNum, pageSize, linkDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable("id") Integer id){
        return linkService.getLinkById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable("id") List<Integer> ids){
        return linkService.deleteLink(ids);
    }

    @PostMapping()
    public ResponseResult addLink(@RequestBody Link link){
        return linkService.addLink(link);
    }

    @PutMapping()
    public ResponseResult updateLink(@RequestBody Link link){
        return linkService.updateLink(link);
    }

    @PutMapping("/changeLinkStatus")
    public ResponseResult changeLinkStatus(@RequestBody LinkDto linkDto){
        return linkService.changeLinkStatus(linkDto);
    }

}
