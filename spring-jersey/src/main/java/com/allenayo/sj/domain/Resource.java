package com.allenayo.sj.domain;

public class Resource {
    private String name; // 文件名
    private String ext; // 文件后缀
    private long size; // 文件大小（单位字节）
    private String path; // 文件在服务器上的地址（绝对地址）
    private String url; // 文件访问路径
    private String createTime; // 创建时间
    private String creator; // 创建人

    public Resource() {
    }

    public Resource(String name, String ext, long size, String path, String url, String createTime, String creator) {
        this.name = name;
        this.ext = ext;
        this.size = size;
        this.path = path;
        this.url = url;
        this.createTime = createTime;
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Resource{");
        sb.append("name='").append(name).append('\'');
        sb.append(", ext='").append(ext).append('\'');
        sb.append(", size=").append(size);
        sb.append(", path='").append(path).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", createTime='").append(createTime).append('\'');
        sb.append(", creator='").append(creator).append('\'');
        sb.append('}');
        return sb.toString();
    }
}