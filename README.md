# Analysis of Social networks using Graphs
How does a video on social media spread and become viral?<br>
Who are the trendsetters and who are the followers in a vast net of users of social media and how do we identify them?

### The scope of this project is to tackle 2 problems.
*     easier problem: Identify trend setters and trend followers.
*     harder problem: Assuming the starting point of a provided user, how many people does a video reach? What does the shape of the new graph look like?
### Implementation
#### Algorithms and Data Structures
<br>
A classic graph using an adjacency list.<br>

*  Vertices will represent individuals.

* Edges will represent relationships.



**Easier Question**: counting the followers of users (outgoing edges) and identify the trendsetters and followers

**Harder Question**: Need to take into account values tied to the circulating videos along with the values of the video ties with the neighboring nodes ( to figure out how many have actually shared the video) and store them in a list.


