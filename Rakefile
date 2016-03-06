require 'nokogiri'

task :default => :release

def read_ver()
  File.open('pom.xml', 'rb') do |f|
    doc = Nokogiri::XML(f.read())
    return doc.at_css('project>version').content
  end
end

task :package do
  sh "mvn clean package"
end

task :release do
  ver = read_ver()
  target_name = "maxpaynestarter-#{ver}"
  release_dir = "release/#{target_name}"
  rm_rf 'release'
  mkdir_p release_dir
  cp 'res/hstart.exe', release_dir
  cp 'res/maxpaynestarter.bat', release_dir
  cp "target/#{target_name}.jar", "#{release_dir}/maxpaynestarter.jar"
  cp 'README.md', release_dir

  cwd = pwd()
  cd release_dir
  sh <<-DOC.strip()
    7z a "#{target_name}.7z" "*"
  DOC
  mv "#{target_name}.7z", ".."
  cd cwd
end
