<@script lang="groovy">
	System.out.println('hello world by script directive');
	name = 'badqiu';
	def sex = 'm';
	return sex;
</@script>

name:${name},sex:${sex!}
