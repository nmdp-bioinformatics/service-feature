/*

    feature-service-jdbi  JDBI feature service.
    Copyright (c) 2014-2015 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.gnu.org/licenses/lgpl.html

*/
package org.nmdp.service.feature.service.jdbi;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;

import org.skife.jdbi.v2.tweak.ResultSetMapper;

import org.nmdp.service.feature.Feature;

/**
 * Feature mapper.
 */
public final class FeatureMapper implements ResultSetMapper<Feature> {
    @Override
    public Feature map(final int index, final ResultSet resultSet, final StatementContext statementContext) throws SQLException {
        String locus = resultSet.getString("locus");
        String term = resultSet.getString("term");
        int rank = resultSet.getInt("rank");
        long accession = resultSet.getLong("accession");
        String sequence = resultSet.getString("sequence");
        return new Feature(locus, term, rank, accession, sequence);
    }
}
