VERSION=$(git describe)
KEY=CD9C0E84

changelog:
	git dch --ignore-branch --snapshot --auto --git-author
		 
release:
	git dch --ignore-branch --release --auto -N $(VERSION) --git-author

package:
	dpkg-buildpackage -A -us -uc

source:
	dpkg-buildpackage -S -k$(KEY)

upload_to_ppa:
	dput ppa:lordgaav/glaciercmd "$(CHANGES)"
