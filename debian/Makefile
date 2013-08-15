VERSION=$(git describe)

changelog:
	git dch --ignore-branch --snapshot --auto
		 
release:
	git dch --ignore-branch --release --auto -N $(VERSION)

package:
	dpkg-buildpackage -A -us -uc
