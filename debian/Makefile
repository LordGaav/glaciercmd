VERSION=$(git describe)

changelog:
	git dch --ignore-branch --snapshot --auto --git-author
		 
release:
	git dch --ignore-branch --release --auto -N $(VERSION) --git-author

package:
	dpkg-buildpackage -A -us -uc
