package com.intallysh.widom.service.blog.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.intallysh.widom.config.SecurityUtil;
import com.intallysh.widom.dto.BlogReqDto;
import com.intallysh.widom.dto.BlogResponseDto;
import com.intallysh.widom.entity.blog.Blog;
import com.intallysh.widom.exception.ResourceNotProcessedException;
import com.intallysh.widom.repo.BlogRepo;
import com.intallysh.widom.service.blog.BlogService;
import com.intallysh.widom.util.ConstantValues;
import com.intallysh.widom.util.Utils;

@Service
public class BlogServiceImpl implements BlogService {

	@Autowired
	private BlogRepo blogRepo;

	@Autowired
	private ModelMapper mapper;

	@Override
	public Map<String, Object> getBlog(String id) {
		Map<String, Object> map = new HashMap<>();
		Blog blog = this.blogRepo.findById(id)
				.orElseThrow(() -> new ResourceNotProcessedException("Please Enter a valid Blog Id"));
		if (blog.isDeleted) {
			throw new ResourceNotProcessedException("Blog is deleted ...");
		}
		Map<String, Object> blogFile = getBlogFile(blog.getFileLocation());
		String filenameExtension = StringUtils.getFilenameExtension(blog.getFileLocation());
		blogFile.put("filenameExtension", filenameExtension);
		map.put("result", blog);
		map.put("blogFile", blogFile);
		map.put("status", "Success");
		map.put("message", "Blog data fetched Successfully ...");
		return map;
	}

	@Override
	public Map<String, Object> createBlog(BlogReqDto blogReqDto) {
		MultipartFile blogImage = blogReqDto.getBlogImage();

		if (blogImage == null || blogImage.isEmpty() || blogImage.getSize() > 5242880) {
			throw new ResourceNotProcessedException("Upload an image that does not exceed 5 MB");
		}
		Map<String, Object> map = new HashMap<>();
		List<String> filesPath = new ArrayList<>();
		Blog blog = this.mapper.map(blogReqDto, Blog.class);
		try {

			blog.setBloggerId(String.valueOf(SecurityUtil.getCurrentUserDetails().getUserId()));
			blog.setModifiedBy(String.valueOf(SecurityUtil.getCurrentUserDetails().getUserId()));
			blog.setBlogUploadedOn(new Timestamp(System.currentTimeMillis()));
			blog.setModifiedOn(new Timestamp(System.currentTimeMillis()));
			blog.setDeleted(false);
			List<MultipartFile> list = new ArrayList<>();
			list.add(blogReqDto.getBlogImage());
			List<Map<String, Object>> uploadFiles = Utils.uploadFiles(list, ConstantValues.FILE_LOCATION + "blog/new");
			Map<String, Object> file = uploadFiles.get(0);
			String fileName = (String) file.get(ConstantValues.FILE_NAME);
			String folderName = (String) file.get(ConstantValues.FOLDER);
			String fileId = (String) file.get(ConstantValues.FILE_ID);
			String filePath = folderName + "/" + fileName;
			blog.setBlogId(fileId);
			filesPath.add(filePath);

			blog.setFileLocation(filePath);
			blog = this.blogRepo.save(blog);

			map.put("message", "Blog created successfully");
			map.put("status", "Success");
			map.put("result", blog);

		} catch (Exception e) {

			e.printStackTrace();
			Utils.deleteFiles(filesPath);
			throw new ResourceNotProcessedException("Blog not created ...");
		}

		return map;
	}

	@Override
	public Map<String, Object> updateBlog(BlogReqDto blogReqDto) {
		Map<String, Object> map = new HashMap<>();
		Blog blog = this.blogRepo.findById(blogReqDto.getBlogId())
				.orElseThrow(() -> new ResourceNotProcessedException("Please Enter a valid Blog Id"));
		if (blog.isDeleted) {
			throw new ResourceNotProcessedException("Blog is deleted ...");
		}
		List<String> filesPath = new ArrayList<>();
		blog.setBlogId(blogReqDto.getBlogId());
		try {
			blog.setBlogDesc1(blogReqDto.getBlogDesc1());
			blog.setBlogTitle(blogReqDto.getBlogTitle());
			;
			blog.setModifiedOn(new Timestamp(System.currentTimeMillis()));
			MultipartFile filePart = blogReqDto.getBlogImage();
			if (filePart != null) {
				List<MultipartFile> list = new ArrayList<>();
				list.add(filePart);
				List<Map<String, Object>> uploadFiles = Utils.uploadFiles(list,
						ConstantValues.FILE_LOCATION + "blog/new");
				Map<String, Object> file = uploadFiles.get(0);
				String fileName = (String) file.get(ConstantValues.FILE_NAME);
				String folderName = (String) file.get(ConstantValues.FOLDER);
				String filePath = folderName + "/" + fileName;
				blog.setFileLocation(filePath);
			}
			String filePath = blog.getFileLocation();
			filesPath.add(filePath);
			blog = this.blogRepo.save(blog);
			map.put("message", "Blog updated successfully");
			map.put("status", "Success");
			map.put("result", blog);

		} catch (Exception e) {
			Utils.deleteFiles(filesPath);
			e.printStackTrace();
			throw new ResourceNotProcessedException("Blog not updated ...");
		}

		return map;
	}

	@Override
	public Map<String, Object> deleteBlog(String blogId) {
		Map<String, Object> map = new HashMap<>();
		Blog blog = this.blogRepo.findById(blogId)
				.orElseThrow(() -> new ResourceNotProcessedException("Please Enter a valid Blog Id"));
		if (blog.isDeleted) {
			throw new ResourceNotProcessedException("Blog is already deleted ...");
		}
		try {
			blog.setDeleted(true);
			this.blogRepo.save(blog);
			map.put("message", "Blog deleted successfully");
			map.put("status", "Success");

		} catch (Exception e) {
			e.printStackTrace();

			throw new ResourceNotProcessedException("Blog not deleted ...");
		}
		return map;
	}

	public Map<String, Object> getBlogFile(String fileLocation) {
		Map<String, Object> map = new HashMap<>();

		String file = fileLocation;
		System.out.println("file ------ : " + file);
		map.put("fileName", file);
		try {
			InputStream in = new FileInputStream(file);
			String base64String = Utils.convertToBase64(in);
			map.put("fileData", base64String);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("------- FIle not found ------");
		}
		return map;
	}

	@Override
	public Map<String, Object> getBlogs(String type, Pageable paging) {
		Map<String, Object> map = new HashMap<>();
		Page<Blog> pageList;
		try {
			if (type.equals("DELETED")) {
				pageList = this.blogRepo.findAllByIsDeleted(true, paging);
			} else {
				pageList = this.blogRepo.findAllByIsDeleted(false, paging);
			}
			if (pageList.hasContent()) {
				List<BlogResponseDto> list = new ArrayList<>();
				List<Blog> content = pageList.getContent();
				content.forEach(c->{
					BlogResponseDto blog = this.mapper.map(c, BlogResponseDto.class);
					Map<String,Object> blogFile = getBlogFile(c.getFileLocation());
					blog.setFileDetails(blogFile);
					list.add(blog);
				});
				map.put("blogList", list);
			} else {
				map.put("blogList", new ArrayList<>());
			}
			map.put("totalPages", pageList.getTotalPages());
			map.put("totalResults", pageList.getTotalElements());
			map.put("currentPage", pageList.getNumber());
			map.put("noOfElements", pageList.getNumberOfElements());
			map.put("isLastPage", pageList.isLast());
			map.put("isFirstPage", pageList.isFirst());
			map.put("message", "Fetched Users successfully");
			map.put("status", "Success");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResourceNotProcessedException("Something went wrong try again ...");
		}
		return map;
	}

}
